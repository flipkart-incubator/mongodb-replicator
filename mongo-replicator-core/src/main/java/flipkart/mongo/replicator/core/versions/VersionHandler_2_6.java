package flipkart.mongo.replicator.core.versions;

import flipkart.mongo.replicator.core.interfaces.IReplicationEventAdaptor;
import flipkart.mongo.replicator.core.interfaces.VersionHandler;
import flipkart.mongo.replicator.core.model.MongoV;

/**
 * Created by pradeep on 17/10/14.
 */
public class VersionHandler_2_6 implements VersionHandler {

    private final static MongoV vFrom = new MongoV(2, 6);
    private final static MongoV vTo = new MongoV(2, 6);

    @Override
    public MongoV getFrom() {
        return vFrom;
    }

    @Override
    public MongoV getTo() {
        return vTo;
    }

    @Override
    public IReplicationEventAdaptor getReplicationEventAdaptor() {
        return null;
    }
}
