package flipkart.mongo.replicator.node;

import com.google.common.util.concurrent.AbstractService;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;
import flipkart.mongo.replicator.core.model.TaskContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pradeep on 09/10/14.
 */
public class ReplicaSetReplicator extends AbstractService {

    private final ExecutorService replicatorExecutor;
    private final ReplicationTask.ReplicationTaskFactory replicationTaskFactory;

    public ReplicaSetReplicator(TaskContext taskContext, ReplicaSetConfig rsConfig) {
        this.replicatorExecutor = Executors.newSingleThreadExecutor();
        this.replicationTaskFactory = new ReplicationTask.ReplicationTaskFactory(taskContext, rsConfig);
    }

    @Override
    protected void doStart() {
        replicatorExecutor.submit(replicationTaskFactory.instance());
    }

    @Override
    protected void doStop() {
        replicatorExecutor.shutdownNow();
    }
}
