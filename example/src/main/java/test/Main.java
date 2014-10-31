package test;

import com.google.common.collect.ImmutableMap;
import flipkart.mongo.replicator.bootstrap.ClusterReplicatorBuilder;
import flipkart.mongo.replicator.cluster.ClusterReplicator;
import flipkart.mongo.replicator.core.interfaces.ICheckPointHandler;
import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;
import flipkart.mongo.replicator.core.model.MongoV;
import flipkart.mongo.replicator.core.model.Node;
import flipkart.mongo.replicator.core.model.ReplicationEvent;
import org.bson.types.BSONTimestamp;

import java.util.ArrayList;
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
        ClusterReplicator replicator = new ClusterReplicatorBuilder()
                .addConfigSvrNode(new Node("w3-cart-svc10.nm.flipkart.com", 27200))
                .withReplicationHandler(new Test())
                .withCheckPoint(new CheckPointHandler())
                .withMongoVersion(new MongoV(2, 6))
                .build();

        replicator.startAsync();
    }

    public static class CheckPointHandler implements ICheckPointHandler {

        @Override
        public void checkPoint(String replicaSetId, BSONTimestamp timestamp) {
            System.out.println("replSet::" + replicaSetId + ", timestamp::(" + timestamp.getTime() + "," + timestamp.getInc() + ")");
        }

        @Override
        public ImmutableMap<String, BSONTimestamp> getAllCheckPoints() {
            return null;
        }

        @Override
        public BSONTimestamp getCheckPoint(String replicaSetId) {
            return null;
        }
    }

    public static class Test implements IReplicationHandler {

        @Override
        public void replicate(ReplicationEvent replicationEvent) {
            System.out.println(replicationEvent.operation);
        }
    }

}
