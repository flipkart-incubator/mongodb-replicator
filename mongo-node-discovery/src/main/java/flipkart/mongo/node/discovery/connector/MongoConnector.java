package flipkart.mongo.node.discovery.connector;

import com.mongodb.*;

import java.net.UnknownHostException;

/**
 * Created by kishan.gajjar on 13/10/14.
 */
public class MongoConnector {

    private MongoConnectionDetails connectionDetails;

    public MongoConnector(MongoConnectionDetails connectionDetails) {
        this.connectionDetails = connectionDetails;
    }

    public DBCollection getDbCollection() {

        DB dbConnection = getDbConnection();
        return dbConnection.getCollection(connectionDetails.getTable());
    }

    public DB getDbConnection() {

        MongoClientOptions.Builder clientParametersBuilder = new MongoClientOptions.Builder();
        clientParametersBuilder.autoConnectRetry(true);
        clientParametersBuilder.socketKeepAlive(true);

        try {
            ServerAddress serverAddress = new ServerAddress(connectionDetails.getServerAddress(), connectionDetails.getPort());
            MongoClient dbClient = new MongoClient(serverAddress, clientParametersBuilder.build());
            return dbClient.getDB(connectionDetails.getDatabase());
        } catch (UnknownHostException ex) {
            throw new RuntimeException(ex);
        }
    }
}
