package flipkart.mongo.replicator.bootstrap;

import com.mongodb.MongoURI;
import flipkart.mongo.node.discovery.NodeDiscover;
import flipkart.mongo.node.discovery.ReplicaDiscover;
import flipkart.mongo.replicator.cluster.ClusterManager;
import flipkart.mongo.replicator.cluster.ClusterReplicator;
import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;
import flipkart.mongo.replicator.core.model.Cluster;
import flipkart.mongo.replicator.core.model.MongoV;
import flipkart.mongo.replicator.core.model.Node;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kishan.gajjar on 30/10/14.
 */
//TODO: replace this with builder
public class ClusterReplicatorBuilder {

    private IReplicationHandler replicationHandler;
    private List<Node> configSvrNodes;
    private MongoV version;

    public ClusterReplicatorBuilder(List<Node> cfgSvrNodes) {
        withConfigSvrNodes(cfgSvrNodes);
    }

    public ClusterReplicatorBuilder withConfigSvrNodes(List<Node> cfgSvrNodes) {
        this.configSvrNodes = cfgSvrNodes;
        return this;
    }

    public ClusterReplicatorBuilder withReplicationHandler(IReplicationHandler replicationHandler) {
        this.replicationHandler = replicationHandler;
        return this;
    }

    public ClusterReplicatorBuilder withMongoVersion(MongoV version) {
        this.version = version;
        return this;
    }

    public ClusterReplicator build() throws Exception {
        ReplicaDiscover replicaDiscover = new ReplicaDiscover();
        List<ReplicaSetConfig> replicaSetConfigs = replicaDiscover.discover(configSvrNodes.get(0));

        for (ReplicaSetConfig replicaSetConfig : replicaSetConfigs) {
            NodeDiscover nodeDiscover = new NodeDiscover();
            nodeDiscover.discover(replicaSetConfig);
        }

        Cluster cluster = new Cluster(replicaSetConfigs, Arrays.asList(configSvrNodes.get(0)) );
        ClusterManager clusterManager = new ClusterManager(cluster);
        return new ClusterReplicator(clusterManager, replicationHandler, version);
    }

}
