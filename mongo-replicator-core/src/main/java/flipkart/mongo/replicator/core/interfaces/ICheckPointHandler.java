package flipkart.mongo.replicator.core.interfaces;

import com.google.common.collect.ImmutableMap;
import org.bson.types.BSONTimestamp;

/**
 * Created by pradeep on 31/10/14.
 */
public interface ICheckPointHandler {

    int getCycleTimeinSecs();

    void checkPoint(String replicaSetId, BSONTimestamp timestamp);

    ImmutableMap<String, BSONTimestamp> getAllCheckPoints();

    BSONTimestamp getCheckPoint(String replicaSetId);

}
