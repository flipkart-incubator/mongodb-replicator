package flipkart.mongo.replicator.core.model;

/**
 * Created by pradeep on 29/10/14.
 */
public class StandAlone {

    public final ReplicaSetConfig instanceConfig;

    public final MongoV version;

    public StandAlone(ReplicaSetConfig instanceConfig, MongoV version) {
        this.instanceConfig = instanceConfig;
        this.version = version;
    }
}
