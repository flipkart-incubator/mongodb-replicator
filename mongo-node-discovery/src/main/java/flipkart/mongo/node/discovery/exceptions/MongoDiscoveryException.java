package flipkart.mongo.node.discovery.exceptions;

import flipkart.mongo.replicator.core.exceptions.MongoReplicatorException;
import flipkart.mongo.replicator.core.exceptions.ReplicatorErrorCode;

/**
 * Created by kishan.gajjar on 03/11/14.
 */
public class MongoDiscoveryException extends MongoReplicatorException {

    public MongoDiscoveryException(ReplicatorErrorCode errorCode) {
        super(errorCode);
    }

    public MongoDiscoveryException(ReplicatorErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
