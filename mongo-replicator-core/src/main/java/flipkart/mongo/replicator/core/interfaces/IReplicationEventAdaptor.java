package flipkart.mongo.replicator.core.interfaces;

import com.mongodb.DBObject;
import flipkart.mongo.replicator.core.model.ReplicationEvent;

/**
 * Created by pradeep on 10/10/14.
 */
public interface IReplicationEventAdaptor {

    ReplicationEvent convert(DBObject dbObject);

}
