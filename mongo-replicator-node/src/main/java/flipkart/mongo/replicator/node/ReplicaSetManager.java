package flipkart.mongo.replicator.node;

import com.mongodb.MongoURI;
import flipkart.mongo.replicator.core.interfaces.ICheckPointHandler;
import flipkart.mongo.replicator.core.model.Node;
import flipkart.mongo.replicator.core.model.NodeState;
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

        for ( Node node : rsConfig.getNodes()) {
            if ( node.getState().equals(NodeState.PRIMARY) ) {
                return node.getMongoURI();
            }
        }

        return null; //HACK
    }

    public ICheckPointHandler getCheckPointHandler() {
        return checkPointHandler;
    }

    // Figure out master changes & provide hook for the ReplicaSetReplicator to communicate & act for the changes

}
