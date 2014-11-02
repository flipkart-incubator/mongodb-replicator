package flipkart.mongo.replicator.cluster;

import com.google.common.util.concurrent.AbstractService;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by pradeep on 29/10/14.
 */
public class ClusterReplicator extends AbstractService {

    private ScheduledExecutorService scheduler;
    private ServiceManager replicasReplicatorServiceManager;

    private Set<Service> replicaSetServices = new LinkedHashSet<Service>();

    public ClusterReplicator(Set<Service> replicaSetServices) {

        this.replicaSetServices = replicaSetServices;
    }

    @Override
    protected void doStart() {

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
//        scheduler = attachScheduler();
    }

    @Override
    protected void doStop() {
        // stopping the scheduler
        if (scheduler != null)
            scheduler.shutdown();

        replicasReplicatorServiceManager.stopAsync();
    }

//    private ScheduledExecutorService attachScheduler() {
//        //TODO: Need to get from config builder
//        long initialDelay = 10;
//        long periodicDelay = 5;
//
//        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//        ClusterDiscoveryScheduler clusterDiscoveryScheduler = new ClusterDiscoveryScheduler(clusterManager.cluster.cfgsvrs);
//        // registering clusterDiscovery for config updates
//        clusterDiscoveryScheduler.registerCallback(clusterManager);
//
//        // starting the scheduler
//        scheduler.scheduleWithFixedDelay(clusterDiscoveryScheduler, initialDelay, periodicDelay, TimeUnit.SECONDS);
//
//        return scheduler;
//    }

    // Callback
}
