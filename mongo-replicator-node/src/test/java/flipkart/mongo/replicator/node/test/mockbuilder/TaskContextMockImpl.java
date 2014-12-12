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

package flipkart.mongo.replicator.node.test.mockbuilder;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import flipkart.mongo.node.discovery.mock.model.MockDBObjects;
import flipkart.mongo.replicator.core.exceptions.DataStoreWriteFailed;
import flipkart.mongo.replicator.core.exceptions.MongoReplicatorException;
import flipkart.mongo.replicator.core.exceptions.ReplicatorErrorCode;
import flipkart.mongo.replicator.core.interfaces.ICheckPointHandler;
import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;
import flipkart.mongo.replicator.core.model.ReplicationEvent;
import org.bson.types.BSONTimestamp;
import org.junit.Assert;

/**
 * Created by kishan.gajjar on 12/12/14.
 */
public class TaskContextMockImpl {

    private static MockDBObjects.BSONVariant bsonVariant = MockDBObjects.BSONVariant.ACTUAL_TIME;

    public static class MockReplicationHandler implements IReplicationHandler {

        private int failureCounter = 0;
        public MockReplicationHandler(MockDBObjects.BSONVariant mockBsonVariant) {
            bsonVariant = mockBsonVariant;
        }

        @Override
        public void replicate(ReplicationEvent replicationEvent) throws MongoReplicatorException {
            if (replicationEvent.v.getTime() == MockDBObjects.BSONVariant.THROW_EXCEPTION.getTime() && failureCounter < 2) {
                failureCounter++;
                throw new DataStoreWriteFailed(ReplicatorErrorCode.EVENT_REPLICATION_FAILED);
            }

            Assert.assertTrue("Validating time in replicationEvent", replicationEvent.v.getTime() == bsonVariant.getTime());
        }
    }

    public static class MockCheckPointHandler implements ICheckPointHandler {

        @Override
        public int getCycleTimeinSecs() {
            return 10;
        }

        @Override
        public void checkPoint(String replicaSetId, BSONTimestamp timestamp) {
            int time = timestamp.getTime();
            Assert.assertTrue("Asserting checkPoint", time == MockDBObjects.BSONVariant.AFTER.getTime() || time == MockDBObjects.BSONVariant.INIT.getTime());
        }

        @Override
        public ImmutableMap<String, BSONTimestamp> getAllCheckPoints() {
            return null;
        }

        @Override
        public BSONTimestamp getCheckPoint(String replicaSetId) {
            if (bsonVariant == MockDBObjects.BSONVariant.INIT)
                return null;
            return new BSONTimestamp(MockDBObjects.BSONVariant.ACTUAL_TIME.getTime(), 1);
        }
    }

    public static class OpLogFilterMock implements Function<ReplicationEvent, Boolean> {
        private int failureCounter = 0;
        
        @Override
        public Boolean apply(ReplicationEvent event) {
            Boolean isValid = event.v.getTime() != MockDBObjects.BSONVariant.NOT_VALID.getTime();
            if (!isValid) {
                failureCounter++;
            }
            if (failureCounter == 1) {
                isValid = null;
            }
            return isValid;
        }

        @Override
        public boolean equals(Object o) {
            return false;
        }
    }
}
