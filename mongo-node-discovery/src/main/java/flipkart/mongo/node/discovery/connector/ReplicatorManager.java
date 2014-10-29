package flipkart.mongo.node.discovery.connector;

import flipkart.mongo.replicator.core.model.ReplicaSetConfig;

import java.util.List;

/**
 * Created by kishan.gajjar on 30/10/14.
 */
public class ReplicatorManager {

    private MongoConnector mongoConnector;

    public ReplicatorManager(MongoConnectionDetails connectionDetails) {
        this.mongoConnector = new MongoConnector(connectionDetails);
    }

    public void startReplication() {

        List<ReplicaSetConfig> replicaSetConfigs = mongoConnector.getReplicaSetConfigs();
    }
}
