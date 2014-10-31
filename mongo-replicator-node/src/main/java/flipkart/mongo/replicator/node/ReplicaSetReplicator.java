package flipkart.mongo.replicator.node;

import com.google.common.util.concurrent.AbstractService;
import com.mongodb.*;
import flipkart.mongo.replicator.core.interfaces.ICheckPointHandler;
import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;
import flipkart.mongo.replicator.core.interfaces.VersionHandler;
import flipkart.mongo.replicator.core.model.MongoV;
import flipkart.mongo.replicator.core.model.ReplicationEvent;
import flipkart.mongo.replicator.core.versions.VersionManager;
import org.bson.types.BSONTimestamp;

import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

/**
 * Created by pradeep on 09/10/14.
 */
public class ReplicaSetReplicator extends AbstractService {

    private final IReplicationHandler replicationHandler;
    private final VersionHandler versionHandler;
    private final ExecutorService replicatorExecutor;
    private final ReplicaSetManager replicaSetManager;
    private final Function<ReplicationEvent, Boolean> oplogFilter;


    public ReplicaSetReplicator(ReplicaSetManager replicaSetManager, IReplicationHandler replicationHandler, MongoV version, Function<ReplicationEvent, Boolean> oplogFilter) {
        this.replicaSetManager = replicaSetManager;
        this.replicationHandler = replicationHandler;
        this.versionHandler = VersionManager.singleton().getVersionHandler(version);
        this.replicatorExecutor = Executors.newSingleThreadExecutor();
        this.oplogFilter = oplogFilter;
    }

    @Override
    protected void doStart() {
        replicatorExecutor.submit(new Runnable() {
            @Override
            public void run() {

                String shardId = replicaSetManager.getRsConfig().shardName;
                ICheckPointHandler cpHandler = replicaSetManager.getCheckPointHandler();

                Mongo client = null;
                try {
                    client = replicaSetManager.getMaster().connect();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }

                DB db = client.getDB("local");

                BSONTimestamp lastCp = cpHandler.getCheckPoint(shardId);

                DBCollection collection = db.getCollection("oplog.rs");
                DBCursor r;
                if ( lastCp == null) {
                    r = collection.find();
                } else {
                    r = collection.find(new BasicDBObject("ts", new BasicDBObject("$gt", lastCp)));
                }
                DBCursor cursor = r.sort(new BasicDBObject("$natural", 1)).addOption(Bytes.QUERYOPTION_TAILABLE);

                while ( cursor.hasNext() ) {
                    DBObject obj = cursor.next();
                    ReplicationEvent event = versionHandler.getReplicationEventAdaptor().convert(obj);

                    if ( oplogFilter.apply(event) ) {
                        replicationHandler.replicate(event);
                    }

                    cpHandler.checkPoint(replicaSetManager.getRsConfig().shardName, event.v);
                }
            }
        });

    }

    @Override
    protected void doStop() {
        replicatorExecutor.shutdownNow();
    }
}
