package flipkart.mongo.replicator.cluster;

import com.google.common.collect.ImmutableList;
import flipkart.mongo.replicator.core.interfaces.ICheckPointHandler;
import flipkart.mongo.node.discovery.interfaces.IDiscoveryCallback;
import flipkart.mongo.node.discovery.utils.DiscoveryUtils;
import flipkart.mongo.replicator.core.model.Cluster;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;
import flipkart.mongo.replicator.node.ReplicaSetManager;

import java.util.List;

/**
 * Created by pradeep on 29/10/14.
 */
public class ClusterManager implements IDiscoveryCallback {

    public final Cluster cluster;
    private final ICheckPointHandler checkPointHandler;

    // Find out:
    // a) shard add/remove,
    // b) Migrate chunks,
    // c) cfgsvc down & reconnnect etc
    // provide hook for the ClusterReplicator to communicate & act for the changes

    public ClusterManager(Cluster cluster, ICheckPointHandler checkPointHandler) {
        this.cluster = cluster;
        this.checkPointHandler = checkPointHandler;
    }

    public ImmutableList<ReplicaSetManager> getReplicaSetManagers() {
        ImmutableList.Builder<ReplicaSetManager> builder = new ImmutableList.Builder<ReplicaSetManager>();
        for ( ReplicaSetConfig rsConfig : cluster.replicaSets ) {
            builder.add(new ReplicaSetManager(rsConfig, checkPointHandler));
        }

        return builder.build();
    }

    @Override
    public void updateReplicaSetConfigs(List<ReplicaSetConfig> replicaSetConfigs) {

        System.out.println("GOT Updated rsConfigs");
        if (DiscoveryUtils.hasReplicaSetsChanged(cluster.replicaSets, replicaSetConfigs)) {
//            TODO: ReplicaSets has been changed.. update the replicator
            System.out.println("RSConfigs has been updated");
        }
    }
}
