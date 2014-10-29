package flipkart.mongo.replicator.core.model;

/**
 * Created by pradeep on 10/10/14.
 */
public class ReplicationEvent {
    public String operation; //enum
    public long epochTime; // TS
    public long inc;

    public Object eventData; // ?

}
