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

import com.google.common.collect.ImmutableList;
import flipkart.mongo.node.discovery.NodeDiscovery;
import flipkart.mongo.node.discovery.exceptions.MongoDiscoveryException;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kishan.gajjar on 08/12/14.
 */
public class ReplicaSetDiscoveryScheduler extends DiscoveryScheduler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ReplicaSetDiscoveryScheduler.class);

    private ReplicaSetConfig replicaSetConfig;

    public ReplicaSetDiscoveryScheduler(ReplicaSetConfig replicaSetConfig) {
        this.replicaSetConfig = replicaSetConfig;
    }

    @Override
    public void run() {
        try {
            NodeDiscovery nodeDiscovery = new NodeDiscovery(this.replicaSetConfig);
            ReplicaSetConfig replicaSetConfig = nodeDiscovery.discover();
            //notifying the callbacks with the updated replicaConfig
            publish(ImmutableList.of(replicaSetConfig));
        } catch (MongoDiscoveryException e) {
            logger.error("Unable to get updated replicaSet configs from NodeDiscovery");
        }
    }
}
