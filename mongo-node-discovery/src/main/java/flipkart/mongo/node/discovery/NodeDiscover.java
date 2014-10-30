package flipkart.mongo.node.discovery;

import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import flipkart.mongo.replicator.core.model.Node;
import flipkart.mongo.replicator.core.model.NodeState;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kishan.gajjar on 30/10/14.
 */
public class NodeDiscover {

    private static final String DB_FOR_DISCOVERY = "admin";

    /**
     * will connect to one of the shard in replica set and update and nodes in the replicaSet
     *
     * @param replicaSetConfig
     */
    public void discover(ReplicaSetConfig replicaSetConfig) {

        List<Node> replicaNodes = replicaSetConfig.getNodes();
        if (replicaNodes.isEmpty())
            return;

        Node replicaNode = replicaNodes.get(0);

        Mongo client = null;
        try {
            client = replicaNode.getMongoURI().connect();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException(); // HACK
        }
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
