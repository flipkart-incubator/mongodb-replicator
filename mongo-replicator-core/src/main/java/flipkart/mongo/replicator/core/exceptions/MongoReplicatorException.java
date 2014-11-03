package flipkart.mongo.replicator.core.exceptions;

/**
 * Created by kishan.gajjar on 03/11/14.
 */
public abstract class MongoReplicatorException extends Exception {

    public MongoReplicatorException(String message) {
        super(message);
    }

    public MongoReplicatorException(String message, Throwable cause) {
        super(message, cause);
    }
}
