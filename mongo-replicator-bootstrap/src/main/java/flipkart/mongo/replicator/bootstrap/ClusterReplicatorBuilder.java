package flipkart.mongo.replicator.bootstrap;

import flipkart.mongo.node.discovery.ReplicaDiscover;
import flipkart.mongo.replicator.cluster.ClusterManager;
import flipkart.mongo.replicator.cluster.ClusterReplicator;
import flipkart.mongo.replicator.core.interfaces.ICheckPointHandler;
import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;
import flipkart.mongo.replicator.core.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by kishan.gajjar on 30/10/14.
 */
//TODO: replace this with builder
public class ClusterReplicatorBuilder {

    private IReplicationHandler replicationHandler;
    private ArrayList<Node> configSvrNodes;
    private MongoV version;
    private ICheckPointHandler checkPointHandler;
    private Function<ReplicationEvent, Boolean> filter;

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

    public ClusterReplicatorBuilder withOplogFilter(Function<ReplicationEvent, Boolean> filter) {
        this.filter = filter;
        return this;
    }


    public ClusterReplicator build() throws Exception {

        Node configSvrNode = configSvrNodes.get(0);
        ReplicaDiscover replicaDiscover = new ReplicaDiscover(configSvrNode);
        List<ReplicaSetConfig> replicaSetConfigs = replicaDiscover.discover();
        Cluster cluster = new Cluster(replicaSetConfigs, configSvrNodes);
        ClusterManager clusterManager = new ClusterManager(cluster, checkPointHandler);
        return new ClusterReplicator(clusterManager, replicationHandler, version, filter);
    }

}
