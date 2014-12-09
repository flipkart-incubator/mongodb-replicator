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

package flipkart.mongo.node.discovery.test.scheduler;

import com.google.common.collect.ImmutableList;
import flipkart.mongo.node.discovery.interfaces.IDiscoveryCallback;
import flipkart.mongo.node.discovery.scheduler.ClusterDiscoveryScheduler;
import flipkart.mongo.node.discovery.test.BaseDiscoveryTest;
import flipkart.mongo.node.discovery.test.mock.MockReplicaSetModel;
import flipkart.mongo.replicator.core.model.Node;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;
import org.junit.Assert;
import org.mockito.Matchers;

import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Created by kishan.gajjar on 09/12/14.
 */
public class ClusterDiscoverySchedulerTest extends BaseDiscoveryTest {
    private static List<Node> mongoNodes;
    private ClusterDiscoveryScheduler clusterDiscoveryScheduler;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        mongoNodes = MockReplicaSetModel.MOCK_MONGO_NODES.subList(1, MockReplicaSetModel.MOCK_MONGO_NODES.size());
        clusterDiscoveryScheduler = new ClusterDiscoveryScheduler(mongoNodes);
    }

    public void testRegisteredCallbacks() throws Exception {
        clusterDiscoveryScheduler.registerCallback(new TestDiscoveryCallback());
        clusterDiscoveryScheduler.run();
    }

    public void testDiscoveryException() {
        when(mockConnectionPool.get(Matchers.anyString())).thenReturn(null);
        clusterDiscoveryScheduler.run();
    }

    public static class TestDiscoveryCallback implements IDiscoveryCallback {
        @Override
        public void updateReplicaSetConfigs(ImmutableList<ReplicaSetConfig> updatedRSConfigs) {
            Assert.assertTrue("DiscoveryCallback replicaSet size", updatedRSConfigs.size() == MockReplicaSetModel.MOCK_MONGO_NODES.size());
        }
    }
}
