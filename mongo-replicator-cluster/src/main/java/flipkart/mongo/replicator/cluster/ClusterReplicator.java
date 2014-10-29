package flipkart.mongo.replicator.cluster;

import flipkart.mongo.replicator.core.interfaces.IReplicationEventAdaptor;
import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;

/**
 * Created by pradeep on 29/10/14.
 */
public class ClusterReplicator {

    public final ClusterManager clusterManager;
    public final IReplicationHandler replicationHandler;
    public final IReplicationEventAdaptor replicationEventAdaptor;

    public ClusterReplicator(ClusterManager clusterManager, IReplicationHandler replicationHandler, IReplicationEventAdaptor replicationEventAdaptor) {
        this.clusterManager = clusterManager;
        this.replicationHandler = replicationHandler;
        this.replicationEventAdaptor = replicationEventAdaptor;
    }
}
