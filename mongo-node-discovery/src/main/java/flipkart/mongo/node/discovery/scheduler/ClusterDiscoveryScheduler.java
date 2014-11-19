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

package flipkart.mongo.node.discovery.scheduler;

import com.google.common.collect.Lists;
import flipkart.mongo.node.discovery.ReplicaDiscovery;
import flipkart.mongo.node.discovery.exceptions.MongoDiscoveryException;
import flipkart.mongo.node.discovery.interfaces.IDiscoveryCallback;
import flipkart.mongo.replicator.core.model.Node;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by kishan.gajjar on 31/10/14.
 */
public class ClusterDiscoveryScheduler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ClusterDiscoveryScheduler.class);

    private List<Node> configSvrNodes;
    private List<IDiscoveryCallback> discoveryCallbacks = Lists.newArrayList();

    public ClusterDiscoveryScheduler(List<Node> configSvrNodes) {
        this.configSvrNodes = configSvrNodes;
    }

    @Override
    public void run() {
        ReplicaDiscovery replicaDiscovery = new ReplicaDiscovery(configSvrNodes);

        try {
            List<ReplicaSetConfig> replicaSetConfigs = replicaDiscovery.discover();
            //notifying the callbacks with the updated replicaConfigs
            publish(replicaSetConfigs);
        } catch (MongoDiscoveryException e) {
            logger.error("Unable to get updated replicaSet configs");
        }
    }

    private void publish(List<ReplicaSetConfig> replicaSetConfigs) {
        for (IDiscoveryCallback discoveryCallback : this.discoveryCallbacks) {
            discoveryCallback.updateReplicaSetConfigs(replicaSetConfigs);
        }
    }

    public void registerCallback(IDiscoveryCallback discoveryCallback) {
        discoveryCallbacks.add(discoveryCallback);
    }
}
