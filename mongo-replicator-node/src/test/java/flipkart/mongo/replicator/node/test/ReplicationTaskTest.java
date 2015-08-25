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

package flipkart.mongo.replicator.node.test;

import flipkart.mongo.node.discovery.mock.model.MockClusterModel;
import flipkart.mongo.node.discovery.mock.model.MockDBObjects;
import flipkart.mongo.replicator.core.model.TaskContext;
import flipkart.mongo.replicator.node.ReplicationTask;
import flipkart.mongo.replicator.node.exceptions.ReplicationTaskException;
import flipkart.mongo.replicator.node.test.mockbuilder.ReplicationTaskMockBuilder;
import org.junit.Assert;

/**
 * Created by kishan.gajjar on 12/12/14.
 */
public class ReplicationTaskTest extends BaseReplicatorNodeTest {

    private ReplicationTask replicationTask;
    private TaskContext taskContext;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        replicaSetConfig = ReplicationTaskMockBuilder.mockReplicaSetWithClient(mockMongoClient);
    }

    public void testUnknownHostException() throws Exception {
        try {
            this.runTest(MockDBObjects.BSONVariant.ACTUAL_TIME);
        } catch (Exception e) {
            Assert.assertSame("UnknownHostException validation", e.getClass(), ReplicationTaskException.class);
        }
    }

    public void testMongoReplicaSetException() throws Exception {
        try {
            this.runTest(MockDBObjects.BSONVariant.ACTUAL_TIME);
        } catch (Exception e) {
            Assert.assertSame("ReplicationTaskException validation", e.getClass(), ReplicationTaskException.class);
        }
    }

    public void testFailureHandler() throws Exception {
        this.runTest(MockDBObjects.BSONVariant.THROW_EXCEPTION);
    }

    public void testActualTimeReplication() throws Exception {
        this.runTest(MockDBObjects.BSONVariant.ACTUAL_TIME);
    }

    public void testBeforeTimeReplication() throws Exception {
        this.runTest(MockDBObjects.BSONVariant.BEFORE);
    }

    public void testAfterTimeReplication() throws Exception {
        this.runTest(MockDBObjects.BSONVariant.AFTER);
    }

    public void testInitReplication() throws Exception {
        this.runTest(MockDBObjects.BSONVariant.INIT);
    }

    public void testInvalidFilter() throws Exception {
        this.runTest(MockDBObjects.BSONVariant.NOT_VALID);
    }

    private void runTest(MockDBObjects.BSONVariant bsonVariant) {
        mockDBCursor = MockClusterModel.mockDBCursorWithDBObject(mockDBCollection, new MockDBObjects().withBSONVariant(bsonVariant));
        taskContext = ReplicationTaskMockBuilder.mockTaskContext(bsonVariant);

        replicationTask = new ReplicationTask.ReplicationTaskFactory(taskContext, replicaSetConfig).instance();
        replicationTask.run();
    }
}
