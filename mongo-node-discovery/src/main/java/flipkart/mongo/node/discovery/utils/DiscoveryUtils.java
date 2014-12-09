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

package flipkart.mongo.node.discovery.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by kishan.gajjar on 31/10/14.
 */
public class DiscoveryUtils {

    /**
     * will compare the replicaSets which are being used with the updated replicaSets
     * @param currentReplicaSets
     * @param updatedReplicaSets
     * @return
     */
    public static boolean hasReplicaSetsChanged(ImmutableList<ReplicaSetConfig> currentReplicaSets, ImmutableList<ReplicaSetConfig> updatedReplicaSets) {

        Map<Integer, ReplicaSetConfig> currentRSConfigMap = DiscoveryUtils.getHashForReplicaSets(currentReplicaSets);
        Map<Integer, ReplicaSetConfig> updatedRSConfigMap = DiscoveryUtils.getHashForReplicaSets(updatedReplicaSets);

        Set<Integer> currentHashCodes = currentRSConfigMap.keySet();
        Set<Integer> updatedHashCodes = updatedRSConfigMap.keySet();

        return !currentHashCodes.containsAll(updatedHashCodes);
    }

    /**
     * assign hashcode to replicaSet, which will be used for comparing two replicaSets
     * @param replicaSetConfigs
     * @return
     */
    private static Map<Integer, ReplicaSetConfig> getHashForReplicaSets(List<ReplicaSetConfig> replicaSetConfigs) {

        Map<Integer, ReplicaSetConfig> rsConfigMap = Maps.newHashMap();

        for (ReplicaSetConfig rsConfig : replicaSetConfigs) {
            rsConfigMap.put(rsConfig.hashCode(), rsConfig);
        }
        return rsConfigMap;
    }
}
