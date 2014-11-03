package flipkart.mongo.replicator.node;

import flipkart.mongo.replicator.core.interfaces.ICheckPointHandler;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;

/**
 * Created by pradeep on 09/10/14.
 */
public class ReplicaSetManager {

    private final ReplicaSetConfig rsConfig;
    private final ICheckPointHandler checkPointHandler;

    public ReplicaSetManager(ReplicaSetConfig rsConfig, ICheckPointHandler checkPointHandler) {
        this.rsConfig = rsConfig;
        this.checkPointHandler = checkPointHandler;
    }

    public ReplicaSetConfig getRsConfig() {
        return rsConfig;
    }

    public ICheckPointHandler getCheckPointHandler() {
        return checkPointHandler;
    }

    // Figure out master changes & provide hook for the ReplicaSetReplicator to communicate & act for the changes

}
