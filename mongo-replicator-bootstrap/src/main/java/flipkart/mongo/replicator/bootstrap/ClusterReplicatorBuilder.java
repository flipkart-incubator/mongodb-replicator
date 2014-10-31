package flipkart.mongo.replicator.bootstrap;

import flipkart.mongo.node.discovery.NodeDiscover;
import flipkart.mongo.node.discovery.ReplicaDiscover;
import flipkart.mongo.replicator.cluster.ClusterManager;
import flipkart.mongo.replicator.cluster.ClusterReplicator;
import flipkart.mongo.replicator.core.interfaces.ICheckPointHandler;
import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;
import flipkart.mongo.replicator.core.model.Cluster;
import flipkart.mongo.replicator.core.model.MongoV;
import flipkart.mongo.replicator.core.model.Node;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kishan.gajjar on 30/10/14.
 */
//TODO: replace this with builder
public class ClusterReplicatorBuilder {

    private IReplicationHandler replicationHandler;
    private ArrayList<Node> configSvrNodes;
    private MongoV version;
    private ICheckPointHandler checkPointHandler;

    public ClusterReplicatorBuilder() {
        configSvrNodes = new ArrayList<Node>();
    }

    public ClusterReplicatorBuilder(ArrayList<Node> cfgSvrNodes) {
        withConfigSvrNodes(cfgSvrNodes);
    }

    public ClusterReplicatorBuilder withConfigSvrNodes(ArrayList<Node> cfgSvrNodes) {
        this.configSvrNodes = cfgSvrNodes;
        return this;
    }

    public ClusterReplicatorBuilder addConfigSvrNode(Node cfgsvrNode) {
        this.configSvrNodes.add(cfgsvrNode);
        return this;
    }

    public ClusterReplicatorBuilder withReplicationHandler(IReplicationHandler replicationHandler) {
        this.replicationHandler = replicationHandler;
        return this;
    }

    public ClusterReplicatorBuilder withMongoVersion(MongoV version) {
        this.version = version;
        return this;
    }

    public ClusterReplicatorBuilder withCheckPoint(ICheckPointHandler checkPointHandler) {
        this.checkPointHandler = checkPointHandler;
        return this;
    }

    public ClusterReplicator build() throws Exception {
        ReplicaDiscover replicaDiscover = new ReplicaDiscover();
        List<ReplicaSetConfig> replicaSetConfigs = replicaDiscover.discover(configSvrNodes.get(0));

        for (ReplicaSetConfig replicaSetConfig : replicaSetConfigs) {
            NodeDiscover nodeDiscover = new NodeDiscover();
            nodeDiscover.discover(replicaSetConfig);
        }

        Cluster cluster = new Cluster(replicaSetConfigs, Arrays.asList(configSvrNodes.get(0)) );
        ClusterManager clusterManager = new ClusterManager(cluster, checkPointHandler);
        return new ClusterReplicator(clusterManager, replicationHandler, version);
    }

}
