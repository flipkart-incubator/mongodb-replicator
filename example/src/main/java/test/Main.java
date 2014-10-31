package test;

import flipkart.mongo.replicator.bootstrap.ClusterReplicatorBuilder;
import flipkart.mongo.replicator.cluster.ClusterReplicator;
import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;
import flipkart.mongo.replicator.core.model.MongoV;
import flipkart.mongo.replicator.core.model.Node;
import flipkart.mongo.replicator.core.model.ReplicationEvent;

import java.util.Arrays;
import java.util.List;

/**
 * Created by pradeep on 09/10/14.
 */
public class Main {

    /*
    - Error Handling
    - Config updates & callback
    - EventAdaptor final changes
    - Check-pointing Interface
    - "Operations & db.coll" filters to be applied on oplog
     */

    public static void main(String args[]) throws Exception {
        List<Node> cfgSvrs = Arrays.asList(new Node("w3-cart-svc10.nm.flipkart.com", 27200));

        ClusterReplicator replicator = new ClusterReplicatorBuilder(cfgSvrs)
                .withReplicationHandler(new Test())
                .withMongoVersion(new MongoV(2, 6))
                .build();

        replicator.startAsync();
    }

    public static class Test implements IReplicationHandler {

        @Override
        public void replicate(ReplicationEvent replicationEvent) {
            System.out.println(replicationEvent.operation);
        }
    }

}
