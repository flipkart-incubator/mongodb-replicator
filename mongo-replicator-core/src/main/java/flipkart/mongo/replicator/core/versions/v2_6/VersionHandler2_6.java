package flipkart.mongo.replicator.core.versions.v2_6;

import flipkart.mongo.replicator.core.interfaces.IReplicationEventAdaptor;
import flipkart.mongo.replicator.core.interfaces.VersionHandler;
import flipkart.mongo.replicator.core.model.MongoV;

/**
 * Created by pradeep on 17/10/14.
 */
public class VersionHandler2_6 implements flipkart.mongo.replicator.core.interfaces.VersionHandler {

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
        return new ReplicationEventAdaptor2_6();
    }
}
