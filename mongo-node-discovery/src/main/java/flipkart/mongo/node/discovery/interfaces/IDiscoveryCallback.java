package flipkart.mongo.node.discovery.interfaces;

import flipkart.mongo.replicator.core.model.ReplicaSetConfig;

import java.util.List;

/**
 * Created by kishan.gajjar on 31/10/14.
 */
public interface IDiscoveryCallback {

    public void updateReplicaSetConfigs(List<ReplicaSetConfig> replicaSetConfigs);
}
