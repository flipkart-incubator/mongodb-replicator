package flipkart.mongo.replicator.node;

import com.mongodb.*;
import flipkart.mongo.replicator.core.interfaces.IReplicationEventAdaptor;
import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;
import flipkart.mongo.replicator.core.model.ReplicationEvent;

/**
 * Created by pradeep on 09/10/14.
 */
public class ReplicaSetReplicator {

    private final IReplicationHandler replicationHandler;
    private ReplicaSetManager replicaSetManager;

    private IReplicationEventAdaptor replicationEventAdaptor;

    public ReplicaSetReplicator(ReplicaSetManager replicaSetManager, IReplicationHandler replicationHandler, IReplicationEventAdaptor replicationEventAdaptor) {
        this.replicaSetManager = replicaSetManager;
        this.replicationHandler = replicationHandler;
        this.replicationEventAdaptor = replicationEventAdaptor;
    }

    public void abc() throws Exception {
        Mongo client = replicaSetManager.getMaster().connect();
        DB db = client.getDB("local");

//        BasicDBObject lastCheckpointTimestamp = new BasicDBObject();
//        BasicDBObject initialWhereFilter = new BasicDBObject();
//        initialWhereFilter.put("ts", lastCheckpointTimestamp);

        DBCursor cursor = db.getCollection("oplog.rs").find().sort(new BasicDBObject("$natural", 1)).addOption(Bytes.QUERYOPTION_TAILABLE);

        while ( cursor.hasNext() ) {
            DBObject obj = cursor.next();
            ReplicationEvent event = replicationEventAdaptor.convert(obj);
            replicationHandler.replicate(event);
        }
    }


}
