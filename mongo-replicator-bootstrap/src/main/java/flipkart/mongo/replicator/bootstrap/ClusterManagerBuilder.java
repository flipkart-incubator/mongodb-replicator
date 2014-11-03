package flipkart.mongo.replicator.bootstrap;

import com.google.common.base.Function;
import flipkart.mongo.node.discovery.ReplicaDiscovery;
import flipkart.mongo.replicator.cluster.ClusterManager;
import flipkart.mongo.replicator.core.exceptions.MongoReplicatorException;
import flipkart.mongo.replicator.core.interfaces.ICheckPointHandler;
import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;
import flipkart.mongo.replicator.core.model.*;
import flipkart.mongo.replicator.core.model.bootstrapconfigs.SchedulerConfigs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kishan.gajjar on 30/10/14.
 */
public class ClusterManagerBuilder {

    private IReplicationHandler replicationHandler;
    private ArrayList<Node> configSvrNodes;
    private MongoV version;
    private ICheckPointHandler checkPointHandler;
    private Function<ReplicationEvent, Boolean> filter;
    private SchedulerConfigs schedulerConfigs = new SchedulerConfigs();

    public ClusterManagerBuilder() {
        configSvrNodes = new ArrayList<Node>();
    }

    public ClusterManagerBuilder(ArrayList<Node> cfgSvrNodes) {
        withConfigSvrNodes(cfgSvrNodes);
    }

    public ClusterManagerBuilder withConfigSvrNodes(ArrayList<Node> cfgSvrNodes) {
        this.configSvrNodes = cfgSvrNodes;
        return this;
    }

    public ClusterManagerBuilder addConfigSvrNode(Node cfgsvrNode) {
        this.configSvrNodes.add(cfgsvrNode);
        return this;
    }

    public ClusterManagerBuilder withReplicationHandler(IReplicationHandler replicationHandler) {
        this.replicationHandler = replicationHandler;
        return this;
    }

    public ClusterManagerBuilder withMongoVersion(MongoV version) {
        this.version = version;
        return this;
    }

    public ClusterManagerBuilder withCheckPoint(ICheckPointHandler checkPointHandler) {
        this.checkPointHandler = checkPointHandler;
        return this;
    }

    public ClusterManagerBuilder withOplogFilter(Function<ReplicationEvent, Boolean> filter) {
        this.filter = filter;
        return this;
    }

    public ClusterManagerBuilder setSchedulerConfigs(long initialDelay, long periodicDelay) {

        this.schedulerConfigs.setInitialDelay(initialDelay);
        this.schedulerConfigs.setPeriodicDelay(periodicDelay);
        return this;
    }

    public ClusterManager build() throws MongoReplicatorException {

        ReplicaDiscovery replicaDiscover = new ReplicaDiscovery(configSvrNodes);
        List<ReplicaSetConfig> replicaSetConfigs = replicaDiscover.discover();
        Cluster cluster = new Cluster(replicaSetConfigs, configSvrNodes);
        return new ClusterManager(cluster, checkPointHandler, replicationHandler, version, filter, schedulerConfigs);
    }

}