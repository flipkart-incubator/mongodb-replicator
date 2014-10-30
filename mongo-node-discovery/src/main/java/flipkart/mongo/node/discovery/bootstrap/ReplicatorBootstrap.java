package flipkart.mongo.node.discovery.bootstrap;

import flipkart.mongo.node.discovery.NodeDiscover;
import flipkart.mongo.node.discovery.ReplicaDiscover;
import flipkart.mongo.replicator.cluster.ClusterManager;
import flipkart.mongo.replicator.core.model.Cluster;
import flipkart.mongo.replicator.core.model.MongoV;
import flipkart.mongo.replicator.core.model.Node;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kishan.gajjar on 30/10/14.
 */
public class ReplicatorBootstrap {

    private String configSvrHost;
    private int configSvrPort;

    public ReplicatorBootstrap(String configSvrHost, int configSvrPort) {
        this.configSvrHost = configSvrHost;
        this.configSvrPort = configSvrPort;
    }

    public void initialize() {

        Node confgSvrNode = new Node(configSvrHost, configSvrPort);
        MongoV version = new MongoV(0, 0);

        ReplicaDiscover replicaDiscover = new ReplicaDiscover();
        List<ReplicaSetConfig> replicaSetConfigs = replicaDiscover.discover(configSvrHost, configSvrPort);

        for (ReplicaSetConfig replicaSetConfig : replicaSetConfigs) {
            NodeDiscover nodeDiscover = new NodeDiscover();
            nodeDiscover.discover(replicaSetConfig);
        }

        Cluster cluster = new Cluster(replicaSetConfigs, Arrays.asList(confgSvrNode), version);
        ClusterManager clusterManager = new ClusterManager(cluster);
    }
}
