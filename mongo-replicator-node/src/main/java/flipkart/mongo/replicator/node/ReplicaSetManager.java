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

package flipkart.mongo.replicator.node;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import flipkart.mongo.node.discovery.interfaces.IDiscoveryCallback;
import flipkart.mongo.node.discovery.scheduler.ReplicaSetDiscoveryScheduler;
import flipkart.mongo.node.discovery.utils.DiscoveryUtils;
import flipkart.mongo.replicator.core.interfaces.ICheckPointHandler;
import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;
import flipkart.mongo.replicator.core.manager.ReplicatorManager;
import flipkart.mongo.replicator.core.model.MongoV;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;
import flipkart.mongo.replicator.core.model.ReplicationEvent;
import flipkart.mongo.replicator.core.model.bootstrapconfigs.SchedulerConfigs;
import flipkart.mongo.replicator.core.versions.VersionManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by pradeep on 09/10/14.
 */
public class ReplicaSetManager extends ReplicatorManager implements IDiscoveryCallback {

    private ReplicaSetConfig replicaSetConfig;
    private Optional<ReplicaSetReplicator> replicaSetReplicator;
    private Optional<ScheduledExecutorService> scheduler;

    public ReplicaSetManager(ReplicaSetConfig replicaSetConfig, ICheckPointHandler checkPointHandler, IReplicationHandler replicationHandler,
                             MongoV version, Function<ReplicationEvent, Boolean> oplogFilter, SchedulerConfigs schedulerConfigs) {

        super(replicationHandler, VersionManager.singleton().getVersionHandler(version), checkPointHandler, oplogFilter, schedulerConfigs);

        this.replicaSetConfig = replicaSetConfig;
    }

    @Override
    public void startReplicator() {
        replicaSetReplicator = Optional.of(new ReplicaSetReplicator(getTaskContext(), replicaSetConfig));
        replicaSetReplicator.get().doStart();

        scheduler = Optional.of(this.attachScheduler());
    }

    @Override
    public void stopReplicator() {
        if (replicaSetReplicator.isPresent())
            replicaSetReplicator.get().doStop();

        // stopping the scheduler
        if (scheduler.isPresent())
            scheduler.get().shutdown();
    }

    @Override
    public void updateReplicaSetConfigs(ImmutableList<ReplicaSetConfig> updatedRSConfigs) {

        if (DiscoveryUtils.hasReplicaSetsChanged(ImmutableList.of(this.replicaSetConfig), updatedRSConfigs)) {

            /**
             * - stopping currently running replicators
             * - updating replicaSetConfigs in cluster
             * - starting replicators with updated configs
             */
            this.stopReplicator();
            this.replicaSetConfig = updatedRSConfigs.get(0);
            this.startReplicator();
        }
    }

    private ScheduledExecutorService attachScheduler() {

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        ReplicaSetDiscoveryScheduler replicaSetDiscoveryScheduler = new ReplicaSetDiscoveryScheduler(this.replicaSetConfig);
        // registering clusterDiscovery for config updates
        replicaSetDiscoveryScheduler.registerCallback(this);

        // starting the scheduler
        scheduler.scheduleWithFixedDelay(replicaSetDiscoveryScheduler, schedulerConfigs.getInitialDelay(), schedulerConfigs.getPeriodicDelay(), TimeUnit.SECONDS);
        return scheduler;
    }
}
