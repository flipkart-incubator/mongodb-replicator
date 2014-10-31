package flipkart.mongo.replicator.node;

import com.google.common.base.Function;
import com.google.common.util.concurrent.AbstractService;
import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;
import flipkart.mongo.replicator.core.interfaces.VersionHandler;
import flipkart.mongo.replicator.core.model.MongoV;
import flipkart.mongo.replicator.core.model.ReplicationEvent;
import flipkart.mongo.replicator.core.versions.VersionManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pradeep on 09/10/14.
 */
public class ReplicaSetReplicator extends AbstractService {

    private final VersionHandler versionHandler;
    private final ExecutorService replicatorExecutor;
    private final ReplicationTask.ReplicationTaskFactory replicationTaskFactory;


    public ReplicaSetReplicator(ReplicaSetManager replicaSetManager, IReplicationHandler replicationHandler, MongoV version, Function<ReplicationEvent, Boolean> oplogFilter) {
        this.versionHandler = VersionManager.singleton().getVersionHandler(version);
        this.replicatorExecutor = Executors.newSingleThreadExecutor();
        this.replicationTaskFactory = new ReplicationTask.ReplicationTaskFactory(replicaSetManager, versionHandler, oplogFilter, replicationHandler);
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
