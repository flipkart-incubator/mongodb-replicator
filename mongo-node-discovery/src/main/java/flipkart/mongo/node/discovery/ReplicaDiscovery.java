package flipkart.mongo.node.discovery;

import com.google.common.collect.Lists;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import flipkart.mongo.node.discovery.exceptions.ConnectionException;
import flipkart.mongo.replicator.core.model.Node;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;

import java.util.List;

/**
 * Created by kishan.gajjar on 30/10/14.
 */
public class ReplicaDiscovery {
    private List<Node> configSvrNodes;
    private static final String CONFIG_DB_NAME = "config";
    private static final String CONFIG_TABLE_NAME = "shards";

    public ReplicaDiscovery(List<Node> configSvrNodes) {
        this.configSvrNodes = configSvrNodes;
    }

    public List<ReplicaSetConfig> discover() {

        List<ReplicaSetConfig> replicaSetConfigs = this.constructReplicaSets();

        for (ReplicaSetConfig replicaSetConfig : replicaSetConfigs) {
            NodeDiscovery nodeDiscovery = new NodeDiscovery(replicaSetConfig);
            nodeDiscovery.discover();
        }

        return replicaSetConfigs;
    }

    private List<ReplicaSetConfig> constructReplicaSets() {

        List<ReplicaSetConfig> replicaSetConfigs = Lists.newArrayList();
        Mongo client = null;
        for (Node configSvrNode : configSvrNodes) {
            try {
                client = MongoConnector.getMongoClient(configSvrNode.getMongoURI());
                break;
            } catch (ConnectionException e) {
                System.out.println("Not able to connect configSvr: " + configSvrNode.getMongoURI());
                e.printStackTrace();
            }
        }

        if (client == null)
            throw new RuntimeException();

        DBCursor dbCursor = client.getDB(CONFIG_DB_NAME).getCollection(CONFIG_TABLE_NAME).find();
        while (dbCursor.hasNext()) {
            DBObject dbObject = dbCursor.next();
            String shardName = (String) dbObject.get("_id");
            String hostString = (String) dbObject.get("host");

            List<Node> replicaNodes = this.getReplicaNodes(hostString);
            if (!replicaNodes.isEmpty()) {
                ReplicaSetConfig replicaConfig = new ReplicaSetConfig(shardName, replicaNodes);
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

            String host = details[0] + ".nm.flipkart.com";
            int port = Integer.parseInt(details[1]);

            replicaNodes.add(new Node(host, port));
        }

        return replicaNodes;
    }
}
