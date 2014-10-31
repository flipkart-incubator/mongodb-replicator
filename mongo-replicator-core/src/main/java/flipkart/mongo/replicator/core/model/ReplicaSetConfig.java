package flipkart.mongo.replicator.core.model;

import com.google.common.base.Optional;

import java.util.List;


/**
 * Created by pradeep on 09/10/14.
 */
public class ReplicaSetConfig {

    public final String shardName;
    private List<Node> nodes;

    public ReplicaSetConfig(String shardName, List<Node> nodes) {
        this.shardName = shardName;
        this.nodes = nodes;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public Optional<Node> findNode(String host, int port) {
        for (Node node : nodes) {
            if (node.host.equals(host) && node.port == port) {
                return Optional.of(node);
            }
        }
        return Optional.absent();
    }

    public Node getMasterNode() {
        for (Node node : nodes) {
            if (node.getState().equals(NodeState.PRIMARY))
                return node;
        }
        return null;
    }

    @Override
    public int hashCode() {
        return (nodes != null ? nodes.hashCode() : 0);
    }
}
