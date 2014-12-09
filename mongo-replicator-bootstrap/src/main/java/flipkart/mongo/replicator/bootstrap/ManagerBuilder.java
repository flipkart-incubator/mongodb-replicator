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

package flipkart.mongo.replicator.bootstrap;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import flipkart.mongo.node.discovery.NodeDiscovery;
import flipkart.mongo.node.discovery.ReplicaDiscovery;
import flipkart.mongo.replicator.cluster.ClusterManager;
import flipkart.mongo.replicator.core.exceptions.MongoReplicatorException;
import flipkart.mongo.replicator.core.interfaces.ICheckPointHandler;
import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;
import flipkart.mongo.replicator.core.model.*;
import flipkart.mongo.replicator.core.model.bootstrapconfigs.SchedulerConfigs;
import flipkart.mongo.replicator.node.ReplicaSetManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kishan.gajjar on 08/12/14.
 */
public class ManagerBuilder {

    private MongoV version;
    private ICheckPointHandler checkPointHandler;
    private Function<ReplicationEvent, Boolean> filter;
    private SchedulerConfigs schedulerConfigs = new SchedulerConfigs();
    private IReplicationHandler replicationHandler;
    private ArrayList<Node> mongoNodes;

    public ManagerBuilder() {
        mongoNodes = Lists.newArrayList();
    }

    public ManagerBuilder(ArrayList<Node> mongoNodes) {
        this.mongoNodes = mongoNodes;
    }

    public ManagerBuilder addMongoNode(Node mongoNode) {
        this.mongoNodes.add(mongoNode);
        return this;
    }

    public ManagerBuilder withMongoNodes(ArrayList<Node> mongoNodes) {
        this.mongoNodes = mongoNodes;
        return this;
    }

    public ManagerBuilder withReplicationHandler(IReplicationHandler replicationHandler) {
        this.replicationHandler = replicationHandler;
        return this;
    }

    public ManagerBuilder withMongoVersion(MongoV version) {
        this.version = version;
        return this;
    }

    public ManagerBuilder withCheckPoint(ICheckPointHandler checkPointHandler) {
        this.checkPointHandler = checkPointHandler;
        return this;
    }

    public ManagerBuilder withOplogFilter(Function<ReplicationEvent, Boolean> filter) {
        this.filter = filter;
        return this;
    }

    public ManagerBuilder setSchedulerConfigs(long initialDelay, long periodicDelay) {

        this.schedulerConfigs.setInitialDelay(initialDelay);
        this.schedulerConfigs.setPeriodicDelay(periodicDelay);
        return this;
    }

    public ReplicaSetManager buildReplicaSetManager() throws MongoReplicatorException {

        NodeDiscovery nodeDiscovery = new NodeDiscovery(new ReplicaSetConfig("ReplicaSetShardName", mongoNodes));
        ReplicaSetConfig replicaSetConfig = nodeDiscovery.discover();

        return new ReplicaSetManager(replicaSetConfig, checkPointHandler, replicationHandler, version, filter, schedulerConfigs);
    }

    public ClusterManager buildClusterManager() throws MongoReplicatorException {

        ReplicaDiscovery replicaDiscover = new ReplicaDiscovery(mongoNodes);
        ImmutableList<ReplicaSetConfig> replicaSetConfigs = replicaDiscover.discover();
        Cluster cluster = new Cluster(replicaSetConfigs, mongoNodes);

        return new ClusterManager(cluster, checkPointHandler, replicationHandler, version, filter, schedulerConfigs);
    }
}
