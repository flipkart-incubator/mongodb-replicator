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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (port != node.port) return false;
        if (host != null ? !host.equals(node.host) : node.host != null) return false;
        if (state != node.state) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = host != null ? host.hashCode() : 0;
        result = 31 * result + port;
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }
}
