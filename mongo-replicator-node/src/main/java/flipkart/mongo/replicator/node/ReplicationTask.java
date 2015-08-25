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

import com.google.common.collect.Lists;
import com.mongodb.CursorType;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import flipkart.mongo.node.discovery.MongoConnector;
import flipkart.mongo.replicator.core.exceptions.MongoReplicatorException;
import flipkart.mongo.replicator.core.model.Node;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;
import flipkart.mongo.replicator.core.model.ReplicationEvent;
import flipkart.mongo.replicator.core.model.TaskContext;
import flipkart.mongo.replicator.node.handler.EventReplicaFailureHandler;
import org.bson.BsonTimestamp;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by pradeep on 31/10/14.
 */

/**
 * - ShardName
 * - Master node
 */
public class ReplicationTask implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ReplicationTask.class);
    private static final int WAIT_FOR_NEXT_ITERATION = 1000; // 1000ms

    private final TaskContext taskContext;
    private final ReplicaSetConfig rsConfig;
    private final EventReplicaFailureHandler eventReplicaFailureHandler;
    private BsonTimestamp lastCp;

    public ReplicationTask(TaskContext taskContext, ReplicaSetConfig rsConfig) {
        this.taskContext = taskContext;
        this.rsConfig = rsConfig;
        this.eventReplicaFailureHandler = new EventReplicaFailureHandler();
    }

    @Override
    public void run() {
        String shardId = rsConfig.shardName;
        Node masterNode = rsConfig.getMasterNode().get();
        MongoClient client = MongoConnector.getMongoClient(Lists.newArrayList(masterNode));

        MongoDatabase database = client.getDatabase("local");
        lastCp = taskContext.checkPointHandler.getCheckPoint(shardId);

        logger.info(String.format("######## START REPLICATOR FOR MongoURI: %s. LastCheckpoint: %s #######", client.getAddress(), lastCp));
        MongoCollection<Document> collection = database.getCollection("oplog.rs");
        FindIterable<Document> iterable;
        MongoCursor<Document> cursor;
        do {
            if (lastCp == null) {
                iterable = collection.find();
            } else {
                iterable = collection.find(new Document("ts", new Document("$gt", lastCp)));
            }
            cursor = iterable.sort(new Document("$natural", 1)).cursorType(CursorType.TailableAwait).iterator();
            try {
                executeCursor(cursor);
                logger.info("Waiting for next iteration");
                Thread.sleep(WAIT_FOR_NEXT_ITERATION);
            } catch (Exception e) {
                logger.error("Exception with cursor.", e);
            } finally {
                cursor.close();
            }
        } while (true);
    }

    private void executeCursor(MongoCursor<Document> cursor) {

        while (cursor.hasNext()) {
            Document obj = cursor.next();
            ReplicationEvent event = taskContext.versionHandler.getReplicationEventAdaptor().convert(obj);
            replicateEvent(event);

            if (lastCp == null ||
                    (event.v.getTime() - lastCp.getTime() >= taskContext.checkPointHandler.getCycleTimeinSecs())) {

                taskContext.checkPointHandler.checkPoint(rsConfig.shardName, event.v);
                lastCp = event.v;
            }
        }
    }

    private void replicateEvent(ReplicationEvent event) {

        Boolean filterApplied = taskContext.oplogFilter.apply(event);
        if (filterApplied != null && filterApplied) {
            boolean eventReplicated = false;
            do {
                try {
                    taskContext.replicationHandler.replicate(event);
                    eventReplicated = true;
                } catch (MongoReplicatorException e) {
                    logger.error("ReplicatorHandler failed.", e);
                    this.eventReplicaFailureHandler.handleFailure(event);
                }
            } while (!eventReplicated);
        }
    }

    public static class ReplicationTaskFactory {
        private final TaskContext taskContext;
        private final ReplicaSetConfig rsConfig;

        public ReplicationTaskFactory(TaskContext taskContext, ReplicaSetConfig rsConfig) {
            this.taskContext = taskContext;
            this.rsConfig = rsConfig;
        }

        public ReplicationTask instance() {
            return new ReplicationTask(taskContext, rsConfig);
        }
    }
}
