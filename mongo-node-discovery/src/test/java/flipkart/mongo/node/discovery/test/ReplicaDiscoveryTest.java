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

package flipkart.mongo.node.discovery.test;

import com.google.common.collect.ImmutableList;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import flipkart.mongo.node.discovery.ReplicaDiscovery;
import flipkart.mongo.node.discovery.exceptions.ConnectionException;
import flipkart.mongo.node.discovery.exceptions.MongoDiscoveryException;
import flipkart.mongo.node.discovery.test.mock.MockClusterModel;
import flipkart.mongo.node.discovery.test.mock.MockDBObjects;
import flipkart.mongo.node.discovery.test.mock.MockReplicaSetModel;
import flipkart.mongo.replicator.core.exceptions.ReplicatorErrorCode;
import flipkart.mongo.replicator.core.model.Node;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;
import org.junit.Assert;
import org.mockito.Matchers;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by kishan.gajjar on 09/12/14.
 */
public class ReplicaDiscoveryTest extends BaseDiscoveryTest {
    private List<Node> mongoNodes;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        mongoNodes = MockReplicaSetModel.MOCK_MONGO_NODES.subList(1, MockReplicaSetModel.MOCK_MONGO_NODES.size());
    }

    public void testEmptyReplicaSets() throws Exception {

        when(mockDBCollection.find()).thenReturn(mock(DBCursor.class));
        ReplicaDiscovery replicaDiscovery = new ReplicaDiscovery(mongoNodes);
        ImmutableList<ReplicaSetConfig> replicaSetConfigs = replicaDiscovery.discover();
        Assert.assertTrue("Empty replicaSets", replicaSetConfigs.isEmpty());
    }

    public void testDiscover() throws Exception {

        MockReplicaSetModel.mockWithEmptyHostState(mockDBCollection);
        ReplicaDiscovery replicaDiscovery = new ReplicaDiscovery(mongoNodes);
        ImmutableList<ReplicaSetConfig> replicaSetConfigs = replicaDiscovery.discover();
        Assert.assertTrue("ReplicaSet size", replicaSetConfigs.size() == MockReplicaSetModel.MOCK_MONGO_NODES.size() - 1);
    }

    public void testNullClient() {

        when(mockConnectionPool.get(Matchers.anyString())).thenReturn(null);
        try {
            new ReplicaDiscovery(mongoNodes).discover();
        } catch (Exception e) {
            Assert.assertSame("Validating MongoDiscoveryException", MongoDiscoveryException.class, e.getClass());
            MongoDiscoveryException discoveryException = (MongoDiscoveryException) e;
            Assert.assertTrue("MongoDiscoveryException errorCode", discoveryException.getErrorCode().equals(ReplicatorErrorCode.REPLICA_SET_MONGO_CLIENT_FAILURE));
        }
    }

    public void testConnectionException() {

        when(mockConnectionPool.get(Matchers.anyString())).thenThrow(ConnectionException.class);
        try {
            new ReplicaDiscovery(mongoNodes).discover();
        } catch (Exception e) {
            Assert.assertSame("Validating MongoDiscoveryException", MongoDiscoveryException.class, e.getClass());
            MongoDiscoveryException discoveryException = (MongoDiscoveryException) e;
            Assert.assertTrue("MongoDiscoveryException errorCode", discoveryException.getErrorCode().equals(ReplicatorErrorCode.REPLICA_SET_MONGO_CLIENT_FAILURE));
        }
    }
}
