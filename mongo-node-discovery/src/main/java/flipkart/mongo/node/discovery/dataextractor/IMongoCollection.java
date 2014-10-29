package flipkart.mongo.node.discovery.dataextractor;

import com.mongodb.DBCollection;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;

import java.util.List;

/**
 * Created by kishan.gajjar on 13/10/14.
 */
public interface IMongoCollection {

    public List<ReplicaSetConfig> getReplicaSetConfigs(DBCollection dbCollection);
}
