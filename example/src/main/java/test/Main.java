package test;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import flipkart.mongo.replicator.bootstrap.ClusterReplicatorBuilder;
import flipkart.mongo.replicator.cluster.ClusterReplicator;
import flipkart.mongo.replicator.core.interfaces.ICheckPointHandler;
import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;
import flipkart.mongo.replicator.core.model.MongoV;
import flipkart.mongo.replicator.core.model.Node;
import flipkart.mongo.replicator.core.model.ReplicationEvent;
import org.bson.types.BSONTimestamp;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by pradeep on 09/10/14.
 */
public class Main {

    /*
    - Error Handling
    - Config updates & callback
    - EventAdaptor final changes
     */

    public static void main(String args[]) throws Exception {
        ClusterReplicator replicator = new ClusterReplicatorBuilder()
//                .addConfigSvrNode(new Node("w3-cart-svc10.nm.flipkart.com", 27200))
                .addConfigSvrNode(new Node("cart-mongo4.nm.flipkart.com", 27200))
//                .addConfigSvrNode(new Node("cart-mongo6.nm.flipkart.com", 27200))
                .withReplicationHandler(new Test())
                .withCheckPoint(new InMemCheckPointHandler())
                .withOplogFilter(new OplogFilter())
                .withMongoVersion(new MongoV(2, 6))
                .build();

        replicator.startAsync();
    }

    public static class InMemCheckPointHandler implements ICheckPointHandler {
        ConcurrentHashMap<String, BSONTimestamp> checkpoint = new ConcurrentHashMap<String, BSONTimestamp>();

        @Override
        public void checkPoint(String replicaSetId, BSONTimestamp timestamp) {
//            System.out.println("replSet::" + replicaSetId + ", timestamp::(" + timestamp.getTime() + "," + timestamp.getInc() + ")");
            checkpoint.put(replicaSetId, timestamp);
        }

        @Override
        public ImmutableMap<String, BSONTimestamp> getAllCheckPoints() {
            return ImmutableMap.copyOf(checkpoint);
        }

        @Override
        public BSONTimestamp getCheckPoint(String replicaSetId) {
            return checkpoint.get(replicaSetId);
        }
    }

    public static class Test implements IReplicationHandler {

        @Override
        public void replicate(ReplicationEvent event) {
            System.out.println("op:" + event.operation + ",ns:" + event.namespace + ",ts:" + event.v );
        }
    }

    private static class OplogFilter implements Function<ReplicationEvent, Boolean> {

        @Override
        public Boolean apply(ReplicationEvent event) {
            return event.namespace.equalsIgnoreCase("cv.o") && event.operation.equalsIgnoreCase("i");
        }
    }
}
