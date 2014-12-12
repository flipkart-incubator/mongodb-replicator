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

package flipkart.mongo.node.discovery.test.utils;

import com.google.common.collect.ImmutableList;
import flipkart.mongo.node.discovery.test.BaseDiscoveryTest;
import flipkart.mongo.node.discovery.mock.model.MockReplicaSetModel;
import flipkart.mongo.node.discovery.utils.DiscoveryUtils;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;
import org.junit.Assert;

/**
 * Created by kishan.gajjar on 09/12/14.
 */
public class DiscoveryUtilsTest extends BaseDiscoveryTest {

    public void testSameReplicaSetsChanged() throws Exception {

        DiscoveryUtils discoveryUtils = new DiscoveryUtils();
        ImmutableList<ReplicaSetConfig> replicaSetConfigsX = ImmutableList.copyOf(MockReplicaSetModel.mockReplicaSetConfigs(3, 3));
        ImmutableList<ReplicaSetConfig> replicaSetConfigsY = ImmutableList.copyOf(MockReplicaSetModel.mockReplicaSetConfigs(3, 3));

        boolean hasReplicaSetsChanged = DiscoveryUtils.hasReplicaSetsChanged(replicaSetConfigsX, replicaSetConfigsY);
        Assert.assertFalse("Asserting same replicaSetConfigs", hasReplicaSetsChanged);
    }

    public void testDiffReplicaSetsChanged() throws Exception {

        ImmutableList<ReplicaSetConfig> replicaSetConfigsX = ImmutableList.copyOf(MockReplicaSetModel.mockReplicaSetConfigs(3, 3));
        ImmutableList<ReplicaSetConfig> replicaSetConfigsY = ImmutableList.copyOf(MockReplicaSetModel.mockReplicaSetConfigs(3, 1));

        boolean hasReplicaSetsChanged = DiscoveryUtils.hasReplicaSetsChanged(replicaSetConfigsX, replicaSetConfigsY);
        Assert.assertTrue("Asserting different replicaSetConfigs", hasReplicaSetsChanged);
    }
}
