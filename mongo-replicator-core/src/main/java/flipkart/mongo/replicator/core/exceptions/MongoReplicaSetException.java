package flipkart.mongo.replicator.core.exceptions;

/**
 * Created by kishan.gajjar on 03/11/14.
 */
public class MongoReplicaSetException extends MongoReplicatorException {

    public MongoReplicaSetException(ReplicatorErrorCode errorCode) {
        super(errorCode);
    }

    public MongoReplicaSetException(ReplicatorErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
