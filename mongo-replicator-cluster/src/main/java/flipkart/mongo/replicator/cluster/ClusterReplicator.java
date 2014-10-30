package flipkart.mongo.replicator.cluster;

import com.google.common.collect.ImmutableList;
import flipkart.mongo.replicator.core.interfaces.IReplicationEventAdaptor;
import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;
import flipkart.mongo.replicator.core.interfaces.VersionHandler;
import flipkart.mongo.replicator.core.model.MongoV;
import flipkart.mongo.replicator.core.versions.VersionManager;
import flipkart.mongo.replicator.node.ReplicaSetManager;
import flipkart.mongo.replicator.node.ReplicaSetReplicator;

/**
 * Created by pradeep on 29/10/14.
 */
public class ClusterReplicator {

    public final ClusterManager clusterManager;
    public final IReplicationHandler replicationHandler;
    public final VersionHandler versionHandler;
    private final MongoV version;

    public ClusterReplicator(ClusterManager clusterManager, IReplicationHandler replicationHandler, MongoV version) {
        this.clusterManager = clusterManager;
        this.replicationHandler = replicationHandler;
        this.version = version;
        versionHandler  = VersionManager.singleton().getVersionHandler(version);
    }

    public void run() throws Exception {
        ImmutableList<ReplicaSetManager> replSetManagers = clusterManager.getReplicaSetManagers();
        new ReplicaSetReplicator(replSetManagers.get(0), replicationHandler, version).abc();
    }
}
