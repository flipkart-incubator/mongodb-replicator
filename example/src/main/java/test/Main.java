package test;

import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;
import flipkart.mongo.replicator.core.model.*;
import flipkart.mongo.replicator.node.ReplicaSetManager;
import flipkart.mongo.replicator.node.ReplicaSetReplicator;

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

        ReplicaSetReplicator replicator = new ReplicaSetReplicator(replicaSetManager, new Test(), new MongoV(2, 6));

        replicator.abc();

    }

    public static class Test implements IReplicationHandler {

        @Override
        public void replicate(ReplicationEvent replicationEvent) {
            System.out.println(replicationEvent.operation);
        }
    }

}
