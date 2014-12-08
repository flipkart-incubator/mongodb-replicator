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

package test;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import flipkart.mongo.replicator.bootstrap.ManagerBuilder;
import flipkart.mongo.replicator.cluster.ClusterManager;
import flipkart.mongo.replicator.core.exceptions.MongoReplicatorException;
import flipkart.mongo.replicator.core.interfaces.ICheckPointHandler;
import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;
import flipkart.mongo.replicator.core.model.MongoV;
import flipkart.mongo.replicator.core.model.Node;
import flipkart.mongo.replicator.core.model.ReplicationEvent;
import org.bson.types.BSONTimestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by pradeep on 09/10/14.
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    /*
    - Error Handling
    - Config updates & callback
    - EventAdaptor final changes
     */

    public static void main(String args[]) {

        try {

            ClusterManager clusterManager = new ManagerBuilder()
                    .addMongoNode(new Node("w3-cart-svc10.nm.flipkart.com", 27200))
                    .withReplicationHandler(new Test())
                    .withCheckPoint(new InMemCheckPointHandler())
                    .withOplogFilter(new OplogFilter())
                    .withMongoVersion(new MongoV(2, 6))
                    .setSchedulerConfigs(10, 5)
                    .buildClusterManager();

            clusterManager.startReplicator();
        } catch (MongoReplicatorException e) {
            logger.error("MongoReplication failed", e);
        }
    }

    public static class InMemCheckPointHandler implements ICheckPointHandler {
        ConcurrentHashMap<String, BSONTimestamp> checkpoint = new ConcurrentHashMap<String, BSONTimestamp>();

        @Override
        public void checkPoint(String replicaSetId, BSONTimestamp timestamp) {
//            System.out.println("replSet::" + replicaSetId + ", timestamp::(" + timestamp.getTime() + "," + timestamp.getInc() + ")");
            checkpoint.put(replicaSetId, timestamp);
        }

        @Override
        public ImmutableMap<String, BSONTimestamp> getAllCheckPoints() {
            return ImmutableMap.copyOf(checkpoint);
        }

        @Override
        public BSONTimestamp getCheckPoint(String replicaSetId) {
            return checkpoint.get(replicaSetId);
        }

        @Override
        public int getCycleTimeinSecs() {
            return 10;
        }
    }

    public static class Test implements IReplicationHandler {

        @Override
        public void replicate(ReplicationEvent event) {
            System.out.println("op:" + event.operation + ",ns:" + event.namespace + ",ts:" + event.v);
        }
    }

    private static class OplogFilter implements Function<ReplicationEvent, Boolean> {

        @Override
        public Boolean apply(ReplicationEvent event) {
            return event.namespace.equalsIgnoreCase("cv.o") && event.operation.equalsIgnoreCase("i");
        }
    }
}
