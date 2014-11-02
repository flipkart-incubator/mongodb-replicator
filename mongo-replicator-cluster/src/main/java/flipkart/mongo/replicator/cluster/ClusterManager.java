package flipkart.mongo.replicator.cluster;

import com.google.common.base.Function;
import com.google.common.util.concurrent.Service;
import flipkart.mongo.node.discovery.interfaces.IDiscoveryCallback;
import flipkart.mongo.node.discovery.utils.DiscoveryUtils;
import flipkart.mongo.replicator.core.interfaces.ICheckPointHandler;
import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;
import flipkart.mongo.replicator.core.interfaces.VersionHandler;
import flipkart.mongo.replicator.core.model.*;
import flipkart.mongo.replicator.core.versions.VersionManager;
import flipkart.mongo.replicator.node.ReplicaSetReplicator;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by pradeep on 29/10/14.
 */
public class ClusterManager implements IDiscoveryCallback {

    public Cluster cluster;
    public final IReplicationHandler replicationHandler;
    private final ICheckPointHandler checkPointHandler;
    public final VersionHandler versionHandler;
    private final Function<ReplicationEvent, Boolean> oplogFilter;

    // Find out:
    // a) shard add/remove,
    // b) Migrate chunks,
    // c) cfgsvc down & reconnnect etc
    // provide hook for the ClusterReplicator to communicate & act for the changes

    public ClusterManager(Cluster cluster, ICheckPointHandler checkPointHandler, IReplicationHandler replicationHandler, MongoV version, Function<ReplicationEvent, Boolean> oplogFilter) {

        this.cluster = cluster;
        this.checkPointHandler = checkPointHandler;
        this.replicationHandler = replicationHandler;
        this.versionHandler = VersionManager.singleton().getVersionHandler(version);
        this.oplogFilter = oplogFilter;
    }

    public void startReplicator() {

        ClusterReplicator clusterReplicator = new ClusterReplicator(this.getReplicaSetReplicators());
        clusterReplicator.doStart();
    }

    private TaskContext getTaskContext() {

        TaskContext context = new TaskContext();
        context.setCheckPointHandler(checkPointHandler);
        context.setOplogFilter(oplogFilter);
        context.setReplicationHandler(replicationHandler);
        context.setVersionHandler(versionHandler);

        return context;
    }

    private Set<Service> getReplicaSetReplicators() {

        Set<Service> replicaSetReplicators = new LinkedHashSet<Service>();
        TaskContext taskContext = this.getTaskContext();
        for (ReplicaSetConfig rsConfig : cluster.replicaSets) {
            replicaSetReplicators.add(new ReplicaSetReplicator(taskContext, rsConfig));
        }

        return replicaSetReplicators;
    }

//    public ImmutableList<ReplicaSetManager> getReplicaSetManagers() {
//        ImmutableList.Builder<ReplicaSetManager> builder = new ImmutableList.Builder<ReplicaSetManager>();
//        for (ReplicaSetConfig rsConfig : cluster.replicaSets) {
//            builder.add(new ReplicaSetManager(rsConfig, checkPointHandler));
//        }
//
//        return builder.build();
//    }

    @Override
    public void updateReplicaSetConfigs(List<ReplicaSetConfig> replicaSetConfigs) {

        System.out.println("GOT Updated rsConfigs");
        if (DiscoveryUtils.hasReplicaSetsChanged(cluster.replicaSets, replicaSetConfigs)) {
            cluster = new Cluster(replicaSetConfigs, cluster.cfgsvrs);
//            TODO: ReplicaSets has been changed.. update the replicator
            System.out.println("RSConfigs has been updated");
        }
    }
}
