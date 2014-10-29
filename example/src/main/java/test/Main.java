package test;

import com.mongodb.DBObject;
import flipkart.mongo.replicator.core.model.Node;
import flipkart.mongo.replicator.core.model.NodeState;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;
import flipkart.mongo.replicator.core.model.ReplicationEvent;
import flipkart.mongo.replicator.node.ReplicaSetManager;
import flipkart.mongo.replicator.node.ReplicaSetReplicator;
import flipkart.mongo.replicator.core.interfaces.IReplicationEventAdaptor;
import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pradeep on 09/10/14.
 */
public class Main {

    public static void main(String args[]) throws Exception {

        List<Node> nodes = new ArrayList<Node>() {{
            add(new Node("cart-mongo3.nm.flipkart.com", 27101, NodeState.PRIMARY));
            add(new Node("lego-svc385.nm.flipkart.com", 27101, NodeState.SECONDARY));
        }};

        ReplicaSetConfig shard1 = new ReplicaSetConfig("shard1", nodes);

        ReplicaSetManager replicaSetManager = new ReplicaSetManager(shard1);

        ReplicaSetReplicator replicator = new ReplicaSetReplicator(replicaSetManager, new Test(), new ReplicationAdaptor());

        replicator.abc();

    }

    public static class Test implements IReplicationHandler {

        @Override
        public void replicate(ReplicationEvent replicationEvent) {
            System.out.println(replicationEvent.operation);
        }
    }

    public static class ReplicationAdaptor implements IReplicationEventAdaptor {

        @Override
        public ReplicationEvent convert(DBObject dbObject) {
            ReplicationEvent event = new ReplicationEvent();
            event.operation = dbObject.get("op").toString();
            return event;
        }
    }
}
