package flipkart.mongo.replicator.cluster;

import com.google.common.util.concurrent.AbstractService;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by pradeep on 29/10/14.
 */
public class ClusterReplicator extends AbstractService {

    private ServiceManager replicasReplicatorServiceManager;
    private Set<Service> replicaSetServices = new LinkedHashSet<Service>();

    public ClusterReplicator(Set<Service> replicaSetServices) {

        this.replicaSetServices = replicaSetServices;
    }

    @Override
    protected void doStart() {
        /**
         * getting set of replicaSetReplicators for defined replicas and
         * attaching them to serviceManager for starting and stopping
         */
        replicasReplicatorServiceManager = new ServiceManager(replicaSetServices);
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

        // waiting for all services to stop
        replicasReplicatorServiceManager.awaitStopped();
    }
}
