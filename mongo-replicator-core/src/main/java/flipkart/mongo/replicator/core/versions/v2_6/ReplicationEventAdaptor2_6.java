package flipkart.mongo.replicator.core.versions.v2_6;

import com.mongodb.DBObject;
import flipkart.mongo.replicator.core.interfaces.IReplicationEventAdaptor;
import flipkart.mongo.replicator.core.model.ReplicationEvent;
import org.bson.types.BSONTimestamp;

/**
 * Created by pradeep on 30/10/14.
 */
public class ReplicationEventAdaptor2_6 implements IReplicationEventAdaptor {

    @Override
    public ReplicationEvent convert(DBObject dbObject) {
        ReplicationEvent event = new ReplicationEvent();
        event.operation = dbObject.get("op").toString();
        event.v = (BSONTimestamp) dbObject.get("ts");
        return event;
    }
}