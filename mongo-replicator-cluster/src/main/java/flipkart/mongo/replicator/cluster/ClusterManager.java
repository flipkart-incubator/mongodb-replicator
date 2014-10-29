package flipkart.mongo.replicator.cluster;

import com.google.common.collect.ImmutableList;
import flipkart.mongo.replicator.core.model.Cluster;
import flipkart.mongo.replicator.node.ReplicaSetManager;

import java.util.List;

/**
 * Created by pradeep on 29/10/14.
 */
public class ClusterManager {

    public final Cluster cluster;

    // Find out:
    // a) shard add/remove,
    // b) Migrate chunks,
    // c) cfgsvc down & reconnnect etc
    // provide hook for the ClusterReplicator to communicate & act for the changes

    public ClusterManager(Cluster cluster) {
        this.cluster = cluster;
    }

    public ImmutableList<ReplicaSetManager> getReplicaSetManagers() {
        ImmutableList.Builder<ReplicaSetManager> builder = new ImmutableList.Builder<ReplicaSetManager>();


        return builder.build();
    }
}
