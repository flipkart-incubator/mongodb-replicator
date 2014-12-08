/*
 * Copyright 2012-2015, the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package flipkart.mongo.replicator.cluster;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import flipkart.mongo.node.discovery.interfaces.IDiscoveryCallback;
import flipkart.mongo.node.discovery.scheduler.ClusterDiscoveryScheduler;
import flipkart.mongo.node.discovery.utils.DiscoveryUtils;
import flipkart.mongo.replicator.core.interfaces.ICheckPointHandler;
import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;
import flipkart.mongo.replicator.core.manager.ReplicatorManager;
import flipkart.mongo.replicator.core.model.Cluster;
import flipkart.mongo.replicator.core.model.MongoV;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;
import flipkart.mongo.replicator.core.model.ReplicationEvent;
import flipkart.mongo.replicator.core.model.bootstrapconfigs.SchedulerConfigs;
import flipkart.mongo.replicator.core.versions.VersionManager;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by pradeep on 29/10/14.
 */
public class ClusterManager extends ReplicatorManager implements IDiscoveryCallback {

    private Cluster cluster;
    private Optional<ClusterReplicator> clusterReplicator;
    private Optional<ScheduledExecutorService> scheduler;

    // Find out:
    // a) shard add/remove,
    // b) Migrate chunks,
    // c) cfgsvc down & reconnnect etc
    // provide hook for the ClusterReplicator to communicate & act for the changes

    public ClusterManager(Cluster cluster, ICheckPointHandler checkPointHandler, IReplicationHandler replicationHandler,
                          MongoV version, Function<ReplicationEvent, Boolean> oplogFilter, SchedulerConfigs schedulerConfigs) {
        super(replicationHandler, VersionManager.singleton().getVersionHandler(version), checkPointHandler, oplogFilter, schedulerConfigs);

        this.cluster = cluster;
    }

    @Override
    public void startReplicator() {

        clusterReplicator = Optional.of(new ClusterReplicator(cluster, getTaskContext()));
        clusterReplicator.get().doStart();

        // attaching the scheduler for updating the RSConfigs
        scheduler = Optional.of(this.attachScheduler());
    }

    @Override
    public void stopReplicator() {

        // stopping clusterReplicator
        if (clusterReplicator.isPresent())
            clusterReplicator.get().doStop();
        // stopping the scheduler
        if (scheduler.isPresent())
            scheduler.get().shutdown();
    }

    @Override
    public void updateReplicaSetConfigs(List<ReplicaSetConfig> updatedRSConfigs) {

        if (DiscoveryUtils.hasReplicaSetsChanged(cluster.getReplicaSets(), updatedRSConfigs)) {

            /**
             * - stopping currently running replicators
             * - updating replicaSetConfigs in cluster
             * - starting replicators with updated configs
             */
            this.stopReplicator();
            this.cluster.setReplicaSets(updatedRSConfigs);
            this.startReplicator();
        }
    }

    private ScheduledExecutorService attachScheduler() {

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        ClusterDiscoveryScheduler clusterDiscoveryScheduler = new ClusterDiscoveryScheduler(cluster.cfgsvrs);
        // registering clusterDiscovery for config updates
        clusterDiscoveryScheduler.registerCallback(this);

        // starting the scheduler
        scheduler.scheduleWithFixedDelay(clusterDiscoveryScheduler, schedulerConfigs.getInitialDelay(), schedulerConfigs.getPeriodicDelay(), TimeUnit.SECONDS);
        return scheduler;
    }
}
