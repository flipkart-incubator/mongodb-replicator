package flipkart.mongo.replicator.core.model;

import com.mongodb.MongoURI;

/**
 * Created by pradeep on 09/10/14.
 */
public class Node {

    public final String host;
    public final int port;

    private NodeState state;

    public Node(String host, int port, NodeState state) {
        this.host = host;
        this.port = port;
        this.state = state;
    }

    public Node(String host, int port) {
        this(host, port, NodeState.UNKNOWN);
    }

    public MongoURI getMongoURI() {
        return new MongoURI("mongodb://" + host + ":" + port );
    }

    public NodeState getState() {
        return state;
    }

    public void setState(NodeState state) {
        this.state = state;
    }
}
