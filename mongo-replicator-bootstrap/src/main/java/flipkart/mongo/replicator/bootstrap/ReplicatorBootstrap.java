package flipkart.mongo.replicator.bootstrap;

import flipkart.mongo.node.discovery.NodeDiscover;
import flipkart.mongo.node.discovery.ReplicaDiscover;
import flipkart.mongo.replicator.cluster.ClusterManager;
import flipkart.mongo.replicator.cluster.ClusterReplicator;
import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;
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

    private final IReplicationHandler replicationHandler;
    private String configSvrHost;
    private int configSvrPort;
    private MongoV version;

    public ReplicatorBootstrap(String configSvrHost, int configSvrPort, IReplicationHandler replicationHandler) {
        this.configSvrHost = configSvrHost;
        this.configSvrPort = configSvrPort;
        this.replicationHandler = replicationHandler;
        this.version = new MongoV(2, 6);
    }

    public void initialize() throws Exception {

        Node confgSvrNode = new Node(configSvrHost, configSvrPort);


        ReplicaDiscover replicaDiscover = new ReplicaDiscover();
        List<ReplicaSetConfig> replicaSetConfigs = replicaDiscover.discover(configSvrHost, configSvrPort);

        for (ReplicaSetConfig replicaSetConfig : replicaSetConfigs) {
            NodeDiscover nodeDiscover = new NodeDiscover();
            nodeDiscover.discover(replicaSetConfig);
        }

        Cluster cluster = new Cluster(replicaSetConfigs, Arrays.asList(confgSvrNode) );
        ClusterManager clusterManager = new ClusterManager(cluster);
        new ClusterReplicator(clusterManager, replicationHandler, version).run();
    }
}
