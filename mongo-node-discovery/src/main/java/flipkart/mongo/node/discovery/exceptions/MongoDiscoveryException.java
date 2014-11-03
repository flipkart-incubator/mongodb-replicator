package flipkart.mongo.node.discovery.exceptions;

import flipkart.mongo.replicator.core.exceptions.MongoReplicatorException;

/**
 * Created by kishan.gajjar on 03/11/14.
 */
public class MongoDiscoveryException extends MongoReplicatorException {

    public MongoDiscoveryException(String message) {
        super(message);
    }

    public MongoDiscoveryException(String message, Throwable cause) {
        super(message, cause);
    }
}
