package test;

import flipkart.mongo.replicator.bootstrap.ClusterReplicatorBootstrap;
import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;
import flipkart.mongo.replicator.core.model.Node;
import flipkart.mongo.replicator.core.model.ReplicationEvent;

import java.util.Arrays;

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
        ClusterReplicatorBootstrap bootstrap = new ClusterReplicatorBootstrap(Arrays.asList(new Node("w3-cart-svc10.nm.flipkart.com", 27200)), new Test());
        bootstrap.initialize();
    }

    public static class Test implements IReplicationHandler {

        @Override
        public void replicate(ReplicationEvent replicationEvent) {
            System.out.println(replicationEvent.operation);
        }
    }

}
