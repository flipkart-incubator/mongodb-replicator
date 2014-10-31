package flipkart.mongo.replicator.cluster;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.AbstractService;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;
import flipkart.mongo.replicator.core.interfaces.VersionHandler;
import flipkart.mongo.replicator.core.model.MongoV;
import flipkart.mongo.replicator.core.versions.VersionManager;
import flipkart.mongo.replicator.node.ReplicaSetManager;
import flipkart.mongo.replicator.node.ReplicaSetReplicator;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by pradeep on 29/10/14.
 */
public class ClusterReplicator extends AbstractService {

    public final ClusterManager clusterManager;
    public final IReplicationHandler replicationHandler;
    public final VersionHandler versionHandler;
    private final MongoV version;
    private ServiceManager replicasReplicatorServiceManager;

    public ClusterReplicator(ClusterManager clusterManager, IReplicationHandler replicationHandler, MongoV version) {
        this.clusterManager = clusterManager;
        this.replicationHandler = replicationHandler;
        this.version = version;
        versionHandler  = VersionManager.singleton().getVersionHandler(version);
    }

    @Override
    protected void doStart() {
        ImmutableList<ReplicaSetManager> replSetManagers = clusterManager.getReplicaSetManagers();
        Set<Service> services = new LinkedHashSet<>();
        for ( ReplicaSetManager replSetManager : replSetManagers) {
            services.add(new ReplicaSetReplicator(replSetManager, replicationHandler, version));
        }

        replicasReplicatorServiceManager = new ServiceManager(services);
        replicasReplicatorServiceManager.addListener(new ServiceManager.Listener() {
            @Override
            public void healthy() {

            }

            @Override
            public void stopped() {

            }

            @Override
            public void failure(Service service) {
                service.startAsync();
            }
        });

        replicasReplicatorServiceManager.startAsync();
    }

    @Override
    protected void doStop() {
        replicasReplicatorServiceManager.stopAsync();
    }
}
