package flipkart.mongo.replicator.bootstrap;

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
public class ClusterReplicatorBootstrap {

    private final IReplicationHandler replicationHandler;
    private final List<Node> configSvrNodes;
    private MongoV version;

    public ClusterReplicatorBootstrap(List<Node> confgSvrNodes, IReplicationHandler replicationHandler) {
        this.configSvrNodes = confgSvrNodes;
        this.replicationHandler = replicationHandler;
        this.version = new MongoV(2, 6);
    }

    public void initialize() throws Exception {

        Node configSvrNode = configSvrNodes.get(0);
        ReplicaDiscover replicaDiscover = new ReplicaDiscover(configSvrNode);
        List<ReplicaSetConfig> replicaSetConfigs = replicaDiscover.discover();

        Cluster cluster = new Cluster(replicaSetConfigs, Arrays.asList(configSvrNode));
        ClusterManager clusterManager = new ClusterManager(cluster);
        ClusterReplicator clusterReplicator = new ClusterReplicator(clusterManager, replicationHandler, version);
        clusterReplicator.startAsync();
    }
}
