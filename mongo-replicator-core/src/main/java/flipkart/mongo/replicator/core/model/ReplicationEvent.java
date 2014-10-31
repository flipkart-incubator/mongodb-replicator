package flipkart.mongo.replicator.core.model;

import org.bson.types.BSONTimestamp;

/**
 * Created by pradeep on 10/10/14.
 */
public class ReplicationEvent {
    public String operation; //enum
    public BSONTimestamp v;

    public Object eventData; // ?

}
