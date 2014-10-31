package flipkart.mongo.replicator.node;

import com.google.common.base.Function;
import com.mongodb.*;
import flipkart.mongo.replicator.core.interfaces.ICheckPointHandler;
import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;
import flipkart.mongo.replicator.core.interfaces.VersionHandler;
import flipkart.mongo.replicator.core.model.ReplicationEvent;
import org.bson.types.BSONTimestamp;

import java.net.UnknownHostException;

/**
 * Created by pradeep on 31/10/14.
 */
public class ReplicationTask implements Runnable {
    private final Function<ReplicationEvent, Boolean> oplogFilter;
    private final VersionHandler versionHandler;
    private final ReplicaSetManager replicaSetManager;
    private final IReplicationHandler replicationHandler;

    public ReplicationTask(ReplicaSetManager replicaSetManager, VersionHandler versionHandler, Function<ReplicationEvent, Boolean> oplogFilter, IReplicationHandler replicationHandler) {
        this.oplogFilter = oplogFilter;
        this.versionHandler = versionHandler;
        this.replicaSetManager = replicaSetManager;
        this.replicationHandler = replicationHandler;
    }

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

    public static class ReplicationTaskFactory {
        private final Function<ReplicationEvent, Boolean> oplogFilter;
        private final VersionHandler versionHandler;
        private final ReplicaSetManager replicaSetManager;
        private final IReplicationHandler replicationHandler;

        public ReplicationTaskFactory(ReplicaSetManager replicaSetManager, VersionHandler versionHandler, Function<ReplicationEvent, Boolean> oplogFilter, IReplicationHandler replicationHandler) {
            this.oplogFilter = oplogFilter;
            this.versionHandler = versionHandler;
            this.replicaSetManager = replicaSetManager;
            this.replicationHandler = replicationHandler;
        }

        public ReplicationTask instance() {
            return new ReplicationTask(replicaSetManager, versionHandler, oplogFilter, replicationHandler);
        }
    }


}
