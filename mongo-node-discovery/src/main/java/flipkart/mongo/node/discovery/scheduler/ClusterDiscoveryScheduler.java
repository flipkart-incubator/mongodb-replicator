package flipkart.mongo.node.discovery.scheduler;

import com.google.common.collect.Lists;
import flipkart.mongo.node.discovery.ReplicaDiscovery;
import flipkart.mongo.node.discovery.exceptions.MongoDiscoveryException;
import flipkart.mongo.node.discovery.interfaces.IDiscoveryCallback;
import flipkart.mongo.replicator.core.model.Node;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by kishan.gajjar on 31/10/14.
 */
public class ClusterDiscoveryScheduler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ClusterDiscoveryScheduler.class);

    private List<Node> configSvrNodes;
    private List<IDiscoveryCallback> discoveryCallbacks = Lists.newArrayList();

    public ClusterDiscoveryScheduler(List<Node> configSvrNodes) {
        this.configSvrNodes = configSvrNodes;
    }

    @Override
    public void run() {
        ReplicaDiscovery replicaDiscovery = new ReplicaDiscovery(configSvrNodes);

        try {
            List<ReplicaSetConfig> replicaSetConfigs = replicaDiscovery.discover();
            //notifying the callbacks with the updated replicaConfigs
            publish(replicaSetConfigs);
        } catch (MongoDiscoveryException e) {
            logger.error("Unable to get updated replicaSet configs");
        }
    }

    private void publish(List<ReplicaSetConfig> replicaSetConfigs) {
        for (IDiscoveryCallback discoveryCallback : this.discoveryCallbacks) {
            discoveryCallback.updateReplicaSetConfigs(replicaSetConfigs);
        }
    }

    public void registerCallback(IDiscoveryCallback discoveryCallback) {
        discoveryCallbacks.add(discoveryCallback);
    }
}
