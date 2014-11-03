package flipkart.mongo.replicator.core.exceptions;

/**
 * Created by kishan.gajjar on 03/11/14.
 */
public enum ReplicatorErrorCode {

    UNKNOWN("Some unknown error"),
    NODE_MONGO_CLIENT_FAILURE("MongoClient for NodeDiscovery is not initiated. Aborting the node discovery"),
    REPLICA_SET_MONGO_CLIENT_FAILURE("MongoClient is not initiated. Aborting the replicaSet discovery"),
    MASTER_NODE_NOT_FOUND("MasterNode not found in the replicaSet");

    protected final String errorMessage;

    ReplicatorErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getMessage() {
        return errorMessage;
    }

}
