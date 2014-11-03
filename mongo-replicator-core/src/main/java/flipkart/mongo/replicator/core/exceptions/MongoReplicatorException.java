package flipkart.mongo.replicator.core.exceptions;

/**
 * Created by kishan.gajjar on 03/11/14.
 */
public abstract class MongoReplicatorException extends Exception {

    public MongoReplicatorException(ReplicatorErrorCode errorCode) {
        super(errorCode.getMessage());
    }

    public MongoReplicatorException(ReplicatorErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
    }
}
