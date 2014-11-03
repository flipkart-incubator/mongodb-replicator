package flipkart.mongo.replicator.core.exceptions;

/**
 * Created by kishan.gajjar on 03/11/14.
 */
public class MongoReplicaSetException extends MongoReplicatorException {

    public MongoReplicaSetException(String message) {
        super(message);
    }

    public MongoReplicaSetException(String message, Throwable cause) {
        super(message, cause);
    }
}
