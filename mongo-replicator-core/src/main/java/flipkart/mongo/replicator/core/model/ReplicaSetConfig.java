package flipkart.mongo.replicator.core.model;

import com.mongodb.MongoURI;

import java.util.List;

/**
 * Created by pradeep on 09/10/14.
 */
public class ReplicaSetConfig {

    public final String shardName;

    public List<Node> nodes;

    public ReplicaSetConfig(String shardName, List<Node> nodes) {
        this.shardName = shardName;
        this.nodes = nodes;
    }

}
