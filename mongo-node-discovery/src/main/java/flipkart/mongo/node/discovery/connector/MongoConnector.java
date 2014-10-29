package flipkart.mongo.node.discovery.connector;

import com.mongodb.*;
import flipkart.mongo.node.discovery.dataextractor.IMongoCollection;
import flipkart.mongo.node.discovery.dataextractor.MongoCollection;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;

import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by kishan.gajjar on 13/10/14.
 */
public class MongoConnector {

    private MongoConnectionDetails connectionDetails;
    private DBCollection dbCollection;

    public MongoConnector(MongoConnectionDetails connectionDetails) {
        this.connectionDetails = connectionDetails;
    }

    public List<ReplicaSetConfig> getReplicaSetConfigs() {
        IMongoCollection mongoCollection = new MongoCollection();
        return mongoCollection.getReplicaSetConfigs(this.getDbCollection());
    }

    private DBCollection getDbCollection() {

        if (this.dbCollection == null) {
            MongoClientOptions.Builder clientParametersBuilder = new MongoClientOptions.Builder();
            clientParametersBuilder.autoConnectRetry(true);
            clientParametersBuilder.socketKeepAlive(true);

            try {
                ServerAddress serverAddress = new ServerAddress(connectionDetails.getServerAddress(), connectionDetails.getPort());
                MongoClient dbClient = new MongoClient(serverAddress, clientParametersBuilder.build());
                DB database = dbClient.getDB(connectionDetails.getDatabase());
                this.dbCollection = database.getCollection(connectionDetails.getTable());

            } catch (UnknownHostException ex) {
                throw new RuntimeException(ex);
            }
        }
        return this.dbCollection;
    }
}
