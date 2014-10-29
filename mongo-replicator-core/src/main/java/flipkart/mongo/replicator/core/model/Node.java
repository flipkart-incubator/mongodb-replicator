package flipkart.mongo.replicator.core.model;

import com.mongodb.MongoURI;

/**
 * Created by pradeep on 09/10/14.
 */
public class Node {

    public final String host;
    public final int port;

    public final NodeState state;

    public Node(String host, int port, NodeState state) {
        this.host = host;
        this.port = port;
        this.state = state;
    }

    public MongoURI getMongoURI() {
        return new MongoURI("mongodb://" + host + ":" + port );
    }
}
