package flipkart.mongo.replicator.node;

import com.mongodb.MongoURI;
import flipkart.mongo.replicator.core.interfaces.ICheckPointHandler;
import flipkart.mongo.replicator.core.model.Node;
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

    public MongoURI getMaster() {

        Node masterNode = rsConfig.getMasterNode();
        if (masterNode != null)
            return masterNode.getMongoURI();

        return null; //HACK
    }

    public ICheckPointHandler getCheckPointHandler() {
        return checkPointHandler;
    }

    // Figure out master changes & provide hook for the ReplicaSetReplicator to communicate & act for the changes

}
