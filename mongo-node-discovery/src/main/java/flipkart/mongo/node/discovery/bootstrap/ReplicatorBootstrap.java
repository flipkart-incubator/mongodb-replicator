package flipkart.mongo.node.discovery.bootstrap;

import flipkart.mongo.node.discovery.connector.MongoConnectionDetails;
import flipkart.mongo.node.discovery.connector.MongoConnector;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;

import java.util.List;

/**
 * Created by kishan.gajjar on 30/10/14.
 */
public class ReplicatorBootstrap {

    private String configSvrHost;
    private int configSvrPort;

    public ReplicatorBootstrap(String configSvrHost, int configSvrPort) {
        this.configSvrHost = configSvrHost;
        this.configSvrPort = configSvrPort;
    }

    public void initialize() {

        MongoConnectionDetails mongoConnectionDetails = new MongoConnectionDetails(configSvrHost, configSvrPort);
        MongoConnector connector = new MongoConnector(mongoConnectionDetails);

//        TODO: pass replica configs to clusterManager
        List<ReplicaSetConfig> replicaSetConfigs = connector.getReplicaSetConfigs();
    }
}
