package flipkart.mongo.replicator.node;

import com.google.common.util.concurrent.AbstractService;
import com.mongodb.*;
import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;
import flipkart.mongo.replicator.core.interfaces.VersionHandler;
import flipkart.mongo.replicator.core.model.MongoV;
import flipkart.mongo.replicator.core.model.ReplicationEvent;
import flipkart.mongo.replicator.core.versions.VersionManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pradeep on 09/10/14.
 */
public class ReplicaSetReplicator extends AbstractService {

    private final IReplicationHandler replicationHandler;
    private final VersionHandler versionHandler;
    private final ExecutorService replicatorExecutor;
    private final ReplicaSetManager replicaSetManager;


    public ReplicaSetReplicator(ReplicaSetManager replicaSetManager, IReplicationHandler replicationHandler, MongoV version) {
        this.replicaSetManager = replicaSetManager;
        this.replicationHandler = replicationHandler;
        this.versionHandler = VersionManager.singleton().getVersionHandler(version);
        this.replicatorExecutor = Executors.newSingleThreadExecutor();
    }

    public void abc() throws Exception {
        Mongo client = replicaSetManager.getMaster().connect();
        DB db = client.getDB("local");

        DBCursor cursor = db.getCollection("oplog.rs").find().sort(new BasicDBObject("$natural", 1)).addOption(Bytes.QUERYOPTION_TAILABLE);

        while ( cursor.hasNext() ) {
            DBObject obj = cursor.next();
            ReplicationEvent event = versionHandler.getReplicationEventAdaptor().convert(obj);
            replicationHandler.replicate(event);
        }
    }


    @Override
    protected void doStart() {

    }

    @Override
    protected void doStop() {

    }
}
