package flipkart.mongo.node.discovery;

import com.google.common.collect.Lists;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import flipkart.mongo.replicator.core.model.Node;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;

import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by kishan.gajjar on 30/10/14.
 */
public class ReplicaDiscover {
    private Node configSvrNode;

    public ReplicaDiscover(Node configSvrNode) {
        this.configSvrNode = configSvrNode;
    }

    public List<ReplicaSetConfig> discover() {

        List<ReplicaSetConfig> replicaSetConfigs = this.constructReplicaSets();

        for (ReplicaSetConfig replicaSetConfig : replicaSetConfigs) {
            NodeDiscover nodeDiscover = new NodeDiscover();
            nodeDiscover.discover(replicaSetConfig);
        }

        return replicaSetConfigs;
    }

    private List<ReplicaSetConfig> constructReplicaSets() {

        List<ReplicaSetConfig> replicaSetConfigs = Lists.newArrayList();
        Mongo client;
        try {
            client = configSvrNode.getMongoURI().connect();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException(); //HACK
        }

        DBCursor dbCursor = client.getDB("config").getCollection("shards").find();
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
