package flipkart.mongo.replicator.node;

import com.mongodb.MongoURI;
import flipkart.mongo.replicator.core.model.Node;
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

        Node masterNode = rsConfig.getMasterNode();
        if (masterNode != null)
            return masterNode.getMongoURI();

        return null; //HACK
    }

    // Figure out master changes & provide hook for the ReplicaSetReplicator to communicate & act for the changes

}
