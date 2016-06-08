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
import flipkart.mongo.replicator.core.exceptions.MongoReplicatorException;
import flipkart.mongo.replicator.core.model.MongoV;
import flipkart.mongo.replicator.core.model.Node;
import flipkart.mongo.replicator.node.ReplicaSetManager;
import flipkart.mongodb.replicator.example.CheckPointExampleHandler;
import flipkart.mongodb.replicator.example.OplogExampleFilter;
import flipkart.mongodb.replicator.example.ReplicationHandlerExample;
import flipkart.mongodb.replicator.example.TestBuilder;

/**
 * Created by kishan.gajjar on 03/12/14.
 */
public class ReplicaSetReplicatorExample {

    public static void main(String args[]) {

        TestBuilder testBuilder = new TestBuilder();
        Node node = testBuilder.getMongosNodeFromArgs(args);
        try {
            ReplicaSetManager replicaSetExample = new ManagerBuilder(node)
                    .withCheckPoint(new CheckPointExampleHandler())
                    .withReplicationHandler(new ReplicationHandlerExample())
                    .withOplogFilter(new OplogExampleFilter())
                    .withMongoVersion(new MongoV(2, 6))
                    .buildReplicaSetManager();

            replicaSetExample.startReplicator();
        } catch (MongoReplicatorException e) {
            e.printStackTrace();
        }
    }

}
