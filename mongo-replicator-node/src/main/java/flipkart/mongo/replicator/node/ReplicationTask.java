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

import com.mongodb.*;
import flipkart.mongo.replicator.core.exceptions.MongoReplicaSetException;
import flipkart.mongo.replicator.core.exceptions.MongoReplicatorException;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;
import flipkart.mongo.replicator.core.model.ReplicationEvent;
import flipkart.mongo.replicator.core.model.TaskContext;
import flipkart.mongo.replicator.node.exceptions.ReplicationTaskException;
import flipkart.mongo.replicator.node.handler.EventReplicaFailureHandler;
import org.bson.types.BSONTimestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;

/**
 * Created by pradeep on 31/10/14.
 */

/**
 * - ShardName
 * - Master node
 */
public class ReplicationTask implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ReplicationTask.class);

    private final TaskContext taskContext;
    private final ReplicaSetConfig rsConfig;
    private final EventReplicaFailureHandler eventReplicaFailureHandler;
    private BSONTimestamp lastCp;

    public ReplicationTask(TaskContext taskContext, ReplicaSetConfig rsConfig) {
        this.taskContext = taskContext;
        this.rsConfig = rsConfig;
        this.eventReplicaFailureHandler = new EventReplicaFailureHandler();
    }

    @Override
    public void run() {
        String shardId = rsConfig.shardName;
        Mongo client;
        try {
            client = rsConfig.getMasterClientURI().connect();
        } catch (MongoReplicaSetException e) {
            throw new ReplicationTaskException("MasterNode not found. RSConfig: " + rsConfig, e);
        } catch (UnknownHostException e) {
            throw new ReplicationTaskException("Connection failure for masterClient. RSConfig: " + rsConfig, e);
        }

        DB db = client.getDB("local");
        lastCp = taskContext.checkPointHandler.getCheckPoint(shardId);

        DBCollection collection = db.getCollection("oplog.rs");
        DBCursor r, cursor;
        do {
            if (lastCp == null) {
                r = collection.find();
            } else {
                r = collection.find(new BasicDBObject("ts", new BasicDBObject("$gt", lastCp)));
            }
            cursor = r.sort(new BasicDBObject("$natural", 1)).addOption(Bytes.QUERYOPTION_TAILABLE);
            try {
                executeCursor(cursor);
            } catch (Exception e) {
                logger.error("Exception with cursor.", e);
            }
        } while (true);
    }

    private void executeCursor(Cursor cursor) {
        while (cursor.hasNext()) {
            DBObject obj = cursor.next();
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
