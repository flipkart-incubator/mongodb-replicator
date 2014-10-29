package flipkart.mongo.replicator.core.interfaces;

import flipkart.mongo.replicator.core.model.ReplicationEvent;

/**
 * Created by pradeep on 10/10/14.
 */
public interface IReplicationHandler {

    void replicate(ReplicationEvent replicationEvent);

}
