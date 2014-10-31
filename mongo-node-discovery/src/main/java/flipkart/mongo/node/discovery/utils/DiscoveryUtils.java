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
    public static boolean hasReplicaSetsChanged(ImmutableList<ReplicaSetConfig> currentReplicaSets, List<ReplicaSetConfig> updatedReplicaSets) {

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
