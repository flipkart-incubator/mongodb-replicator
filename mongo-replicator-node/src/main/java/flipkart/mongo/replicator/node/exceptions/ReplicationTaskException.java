package flipkart.mongo.replicator.node.exceptions;

/**
 * Created by kishan.gajjar on 03/11/14.
 */
public class ReplicationTaskException extends RuntimeException {

    public ReplicationTaskException(String message) {
        super(message);
    }

    public ReplicationTaskException(String message, Throwable cause) {
        super(message, cause);
    }
}
