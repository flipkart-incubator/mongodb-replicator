package flipkart.mongo.replicator.node;

import com.mongodb.MongoURI;
import flipkart.mongo.replicator.core.model.Node;
import flipkart.mongo.replicator.core.model.NodeState;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;

/**
 * Created by pradeep on 09/10/14.
 */
public class ReplicaSetManager {

    private final ReplicaSetConfig rsConfig;

    public ReplicaSetManager(ReplicaSetConfig rsConfig) {
        this.rsConfig = rsConfig;
    }

    public MongoURI getMaster() {

        for ( Node node : rsConfig.nodes) {
            if ( node.state.equals(NodeState.PRIMARY) ) {
                return node.getMongoURI();
            }
        }

        return null; //HACK
    }

}
