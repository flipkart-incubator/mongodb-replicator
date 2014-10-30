package flipkart.mongo.node.discovery.dataextractor;

import com.google.common.collect.Lists;
import com.mongodb.*;
import flipkart.mongo.replicator.core.model.Node;
import flipkart.mongo.replicator.core.model.NodeState;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kishan.gajjar on 13/10/14.
 */
public class MongoCollection implements IMongoCollection {

    @Override
    public List<ReplicaSetConfig> getReplicaSetConfigs(DBCollection dbCollection) {

        List<ReplicaSetConfig> replicaSetConfigs = Lists.newArrayList();
        DBCursor dbCursor = dbCollection.find();

        while (dbCursor.hasNext()) {

            DBObject dbObject = dbCursor.next();
            String shardName = (String) dbObject.get("_id");
            String hostString = (String) dbObject.get("host");

            List<Node> replicaNodes = this.getReplicaNodes(hostString);
            if (!replicaNodes.isEmpty()) {
                ReplicaSetConfig replicaConfig = new ReplicaSetConfig(shardName, replicaNodes);
                updateReplicaSet(replicaConfig);
                replicaSetConfigs.add(replicaConfig);
            }
        }

        return replicaSetConfigs;
    }

    private List<Node> getReplicaNodes(String hostString) {

        List<Node> replicaNodes = Lists.newArrayList();
        String[] hostsInfo = hostString.split(",");

        for (String hostInfo : hostsInfo) {

            String[] data = hostInfo.split("/");
            String hostPortInfo = data.length > 1 ? data[1] : data[0];
            String[] details = hostPortInfo.split(":");

            String host = details[0];
            int port = Integer.parseInt(details[1]);

            replicaNodes.add(new Node(host, port));
        }

        return replicaNodes;
    }

    private void updateReplicaSet(ReplicaSetConfig replicaSetConfig) {

        List<Node> replicaNodes = replicaSetConfig.getNodes();

        if (replicaNodes.isEmpty())
            return;

        Node replicaNode = replicaNodes.get(0);

        MongoClientOptions.Builder clientParametersBuilder = new MongoClientOptions.Builder();
        clientParametersBuilder.autoConnectRetry(true);
        clientParametersBuilder.socketKeepAlive(true);

        try {
            ServerAddress serverAddress = new ServerAddress(replicaNode.host, replicaNode.port);
            MongoClient dbClient = new MongoClient(serverAddress, clientParametersBuilder.build());
            DB database = dbClient.getDB("admin");
            CommandResult replSetGetStatus = database.command("replSetGetStatus");
            List<DBObject> dbDataList = (ArrayList) replSetGetStatus.get("members");
            for (DBObject dbObject : dbDataList) {
                updateReplicaNodeStatus(dbObject, replicaSetConfig);
            }

            System.out.println("STATS: " + replSetGetStatus);
        } catch (UnknownHostException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void updateReplicaNodeStatus(DBObject dbObject, ReplicaSetConfig replicaSetConfig) {

        String mongoUri = (String) dbObject.get("name");
        String[] hostData = mongoUri.split(":");
        String host = hostData[0];
        int port = Integer.parseInt(hostData[1]);
        Node replicaNode = replicaSetConfig.nodeWithConfigs(host, port);

        String nodeState = (String) dbObject.get("stateStr");
        if (NodeState.DB_STATE_MAP.containsKey(nodeState)) {
            replicaNode.setState(NodeState.DB_STATE_MAP.get(nodeState));
        }
    }
}
