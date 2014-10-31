package flipkart.mongo.replicator.core.model;

import com.mongodb.DBObject;
import org.bson.types.BSONTimestamp;

/**
 * Created by pradeep on 10/10/14.
 */
public class ReplicationEvent {
    public final String operation; //enum
    public final BSONTimestamp v;
    public final long h;
    public final String namespace;
    public final DBObject eventData;

    public ReplicationEvent(String operation, BSONTimestamp v, long h, String namespace, DBObject eventData) {
        this.operation = operation;
        this.v = v;
        this.h = h;
        this.namespace = namespace;
        this.eventData = eventData;
    }
}
