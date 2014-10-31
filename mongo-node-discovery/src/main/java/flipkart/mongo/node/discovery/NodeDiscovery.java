package flipkart.mongo.node.discovery;

import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import flipkart.mongo.node.discovery.exceptions.ConnectionException;
import flipkart.mongo.replicator.core.model.Node;
import flipkart.mongo.replicator.core.model.NodeState;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kishan.gajjar on 30/10/14.
 */
public class NodeDiscovery {

    private ReplicaSetConfig replicaSetConfig;
    private static final String DB_FOR_DISCOVERY = "admin";

    public NodeDiscovery(ReplicaSetConfig replicaSetConfig) {
        this.replicaSetConfig = replicaSetConfig;
    }

    /**
     * will connect to one of the shard in replica set and update and nodes in the replicaSet
     */
    public void discover() {

        Mongo client = null;
        for (Node replicaNode : replicaSetConfig.getNodes()) {
            try {
                client = MongoConnector.getMongoClient(replicaNode.getMongoURI());
                break;
            } catch (ConnectionException e) {
                System.out.println("Not able to connect to replicaNode: " + replicaNode.getMongoURI());
                e.printStackTrace();
            }
        }

        if (client == null)
            return;

        DB dbConnection = client.getDB(DB_FOR_DISCOVERY);
        CommandResult replSetGetStatus = dbConnection.command("replSetGetStatus");
        List<DBObject> dbDataList = (ArrayList<DBObject>) replSetGetStatus.get("members");
        for (DBObject dbObject : dbDataList) {
            updateReplicaNodeStatus(dbObject, replicaSetConfig);
        }
    }

    private void updateReplicaNodeStatus(DBObject dbObject, ReplicaSetConfig replicaSetConfig) {

        String mongoUri = (String) dbObject.get("name");
        String[] hostData = mongoUri.split(":");
        String host = hostData[0] + ".nm.flipkart.com"; //HACK
        int port = Integer.parseInt(hostData[1]);
        Node replicaNode = replicaSetConfig.nodeWithConfigs(host, port);

        String nodeState = (String) dbObject.get("stateStr");
        if (NodeState.DB_STATE_MAP.containsKey(nodeState)) {
            replicaNode.setState(NodeState.DB_STATE_MAP.get(nodeState));
        }
    }
}
