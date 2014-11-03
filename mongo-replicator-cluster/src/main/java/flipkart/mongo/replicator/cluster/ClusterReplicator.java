package flipkart.mongo.replicator.cluster;

import com.google.common.util.concurrent.AbstractService;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import flipkart.mongo.replicator.core.model.Cluster;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;
import flipkart.mongo.replicator.core.model.TaskContext;
import flipkart.mongo.replicator.node.ReplicaSetReplicator;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by pradeep on 29/10/14.
 */
public class ClusterReplicator extends AbstractService {

    private ServiceManager replicasReplicatorServiceManager;
    private final Cluster cluster;
    private TaskContext taskContext;

    public ClusterReplicator(Cluster cluster, TaskContext taskContext) {

        this.cluster = cluster;
        this.taskContext = taskContext;
    }

    @Override
    protected void doStart() {

        Set<Service> replicaSetServices = new LinkedHashSet<Service>();
        for (ReplicaSetConfig rsConfig : cluster.getReplicaSets()) {
            replicaSetServices.add(new ReplicaSetReplicator(taskContext, rsConfig));
        }

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
        replicasReplicatorServiceManager.stopAsync();
    }
}
