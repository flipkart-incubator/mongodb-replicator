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
import com.google.common.collect.Lists;
import flipkart.mongo.node.discovery.NodeDiscovery;
import flipkart.mongo.node.discovery.exceptions.MongoDiscoveryException;
import flipkart.mongo.replicator.core.interfaces.ICheckPointHandler;
import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;
import flipkart.mongo.replicator.core.model.MongoV;
import flipkart.mongo.replicator.core.model.Node;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;
import flipkart.mongo.replicator.core.model.ReplicationEvent;
import flipkart.mongo.replicator.node.ReplicaSetManager;

import java.util.ArrayList;

/**
 * Created by kishan.gajjar on 03/12/14.
 */
public class ReplicaSetManagerBuilder {
    private IReplicationHandler replicationHandler;
    private ArrayList<Node> mongoNodes;
    private MongoV version;
    private ICheckPointHandler checkPointHandler;
    private Function<ReplicationEvent, Boolean> filter;

    public ReplicaSetManagerBuilder() {
        mongoNodes = Lists.newArrayList();
    }

    public ReplicaSetManagerBuilder(ArrayList<Node> mongoNodes) {
        this.mongoNodes = mongoNodes;
    }

    public ReplicaSetManagerBuilder addMongoNode(Node mongoNode) {
        this.mongoNodes.add(mongoNode);
        return this;
    }

    public ReplicaSetManagerBuilder withReplicationHandler(IReplicationHandler replicationHandler) {
        this.replicationHandler = replicationHandler;
        return this;
    }

    public ReplicaSetManagerBuilder withMongoVersion(MongoV version) {
        this.version = version;
        return this;
    }

    public ReplicaSetManagerBuilder withCheckPoint(ICheckPointHandler checkPointHandler) {
        this.checkPointHandler = checkPointHandler;
        return this;
    }

    public ReplicaSetManagerBuilder withOplogFilter(Function<ReplicationEvent, Boolean> filter) {
        this.filter = filter;
        return this;
    }

    public ReplicaSetManager build() throws MongoDiscoveryException {

        NodeDiscovery nodeDiscovery = new NodeDiscovery(new ReplicaSetConfig("ReplicaSetShardName", mongoNodes));
        ReplicaSetConfig replicaSetConfig = nodeDiscovery.discover();

        return new ReplicaSetManager(replicaSetConfig, checkPointHandler, replicationHandler, version, filter);
    }
}
