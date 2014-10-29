package flipkart.mongo.replicator.core.interfaces;

import flipkart.mongo.replicator.core.model.MongoV;

/**
 * Created by pradeep on 17/10/14.
 */
public interface VersionHandler {

    MongoV getFrom();

    MongoV getTo();

    IReplicationEventAdaptor getReplicationEventAdaptor();

}
