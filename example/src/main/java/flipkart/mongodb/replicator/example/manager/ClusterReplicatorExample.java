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

package flipkart.mongodb.replicator.example.manager;

import flipkart.mongo.replicator.bootstrap.ManagerBuilder;
import flipkart.mongo.replicator.cluster.ClusterManager;
import flipkart.mongo.replicator.core.exceptions.MongoReplicatorException;
import flipkart.mongo.replicator.core.model.MongoV;
import flipkart.mongo.replicator.core.model.Node;
import flipkart.mongodb.replicator.example.CheckPointExampleHandler;
import flipkart.mongodb.replicator.example.OplogExampleFilter;
import flipkart.mongodb.replicator.example.ReplicationHandlerExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kishan.gajjar on 03/12/14.
 */
public class ClusterReplicatorExample {
    private static final Logger logger = LoggerFactory.getLogger(ClusterReplicatorExample.class);

    public static void main(String args[]) {

        try {
            ClusterManager clusterManager = new ManagerBuilder(new Node("w3-cart-svc10.nm.flipkart.com", 27200))
                    .withReplicationHandler(new ReplicationHandlerExample())
                    .withCheckPoint(new CheckPointExampleHandler())
                    .withOplogFilter(new OplogExampleFilter())
                    .withMongoVersion(new MongoV(2, 6))
                    .setSchedulerConfigs(10, 5)
                    .buildClusterManager();

            clusterManager.startReplicator();
        } catch (MongoReplicatorException e) {
            logger.error("MongoReplication failed", e);
        }
    }
}
