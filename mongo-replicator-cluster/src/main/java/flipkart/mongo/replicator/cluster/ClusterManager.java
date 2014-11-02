package flipkart.mongo.replicator.cluster;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.util.concurrent.Service;
import flipkart.mongo.node.discovery.interfaces.IDiscoveryCallback;
import flipkart.mongo.node.discovery.scheduler.ClusterDiscoveryScheduler;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by pradeep on 29/10/14.
 */
public class ClusterManager implements IDiscoveryCallback {

    private Cluster cluster;
    public final IReplicationHandler replicationHandler;
    private final ICheckPointHandler checkPointHandler;
    public final VersionHandler versionHandler;
    private final Function<ReplicationEvent, Boolean> oplogFilter;
    private Optional<ClusterReplicator> clusterReplicator;
    private Optional<ScheduledExecutorService> scheduler;

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

        ClusterReplicator replicator = new ClusterReplicator(this.getReplicaSetReplicators());
        clusterReplicator = Optional.of(replicator);
        replicator.doStart();

        // attaching the scheduler for updating the RSConfigs
        scheduler = Optional.of(this.attachScheduler());
    }

    public void stopReplicator() {

        // stopping clusterReplicator
        if (clusterReplicator.isPresent())
            clusterReplicator.get().doStop();
        // stopping the scheduler
        if (scheduler.isPresent())
            scheduler.get().shutdown();
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
        for (ReplicaSetConfig rsConfig : cluster.getReplicaSets()) {
            replicaSetReplicators.add(new ReplicaSetReplicator(taskContext, rsConfig));
        }

        return replicaSetReplicators;
    }

    @Override
    public void updateReplicaSetConfigs(List<ReplicaSetConfig> updatedRSConfigs) {

        System.out.println("GOT Updated rsConfigs");
        if (DiscoveryUtils.hasReplicaSetsChanged(cluster.getReplicaSets(), updatedRSConfigs)) {

            /**
             * - stopping currently running replicators
             * - updating replicaSetConfigs in cluster
             * - starting replicators with updated configs
             */
            this.stopReplicator();
            this.cluster.setReplicaSets(updatedRSConfigs);
            this.startReplicator();
            System.out.println("RSConfigs has been updated");
        }
    }

    private ScheduledExecutorService attachScheduler() {
        //TODO: Need to get from config builder
        long initialDelay = 10;
        long periodicDelay = 5;

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        ClusterDiscoveryScheduler clusterDiscoveryScheduler = new ClusterDiscoveryScheduler(cluster.cfgsvrs);
        // registering clusterDiscovery for config updates
        clusterDiscoveryScheduler.registerCallback(this);

        // starting the scheduler
        scheduler.scheduleWithFixedDelay(clusterDiscoveryScheduler, initialDelay, periodicDelay, TimeUnit.SECONDS);
        return scheduler;
    }
}
