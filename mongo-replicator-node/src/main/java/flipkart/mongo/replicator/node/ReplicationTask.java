package flipkart.mongo.replicator.node;

import com.mongodb.*;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;
import flipkart.mongo.replicator.core.model.ReplicationEvent;
import flipkart.mongo.replicator.core.model.TaskContext;
import org.bson.types.BSONTimestamp;

import java.net.UnknownHostException;

/**
 * Created by pradeep on 31/10/14.
 */

/**
 * - ShardName
 * - Master node
 */
public class ReplicationTask implements Runnable {
    private final TaskContext taskContext;
    private final ReplicaSetConfig rsConfig;

    public ReplicationTask(TaskContext taskContext, ReplicaSetConfig rsConfig) {
        this.taskContext = taskContext;
        this.rsConfig = rsConfig;
    }

    @Override
    public void run() {
        String shardId = rsConfig.shardName;

        Mongo client;
        try {
            client = rsConfig.getMasterClientURI().connect();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        DB db = client.getDB("local");

        BSONTimestamp lastCp = taskContext.getCheckPointHandler().getCheckPoint(shardId);

        DBCollection collection = db.getCollection("oplog.rs");
        DBCursor r;
        if (lastCp == null) {
            r = collection.find();
        } else {
            r = collection.find(new BasicDBObject("ts", new BasicDBObject("$gt", lastCp)));
        }
        DBCursor cursor = r.sort(new BasicDBObject("$natural", 1)).addOption(Bytes.QUERYOPTION_TAILABLE);

        while (cursor.hasNext()) {
            DBObject obj = cursor.next();
            ReplicationEvent event = taskContext.getVersionHandler().getReplicationEventAdaptor().convert(obj);

            if (taskContext.getOplogFilter().apply(event)) {
                taskContext.getReplicationHandler().replicate(event);
            }

            taskContext.getCheckPointHandler().checkPoint(rsConfig.shardName, event.v);
        }
    }

    public static class ReplicationTaskFactory {
        private final TaskContext taskContext;
        private final ReplicaSetConfig rsConfig;

        public ReplicationTaskFactory(TaskContext taskContext, ReplicaSetConfig rsConfig) {
            this.taskContext = taskContext;
            this.rsConfig = rsConfig;
        }

        public ReplicationTask instance() {
            return new ReplicationTask(taskContext, rsConfig);
        }
    }
}
