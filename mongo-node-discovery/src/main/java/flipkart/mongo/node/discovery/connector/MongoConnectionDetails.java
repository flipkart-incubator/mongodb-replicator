package flipkart.mongo.node.discovery.connector;

/**
 * Created by kishan.gajjar on 13/10/14.
 */
public class MongoConnectionDetails {

    private String serverAddress;
    private int port;
    private final String DATABASE = "config";
    private final String TABLE = "shards";

    public MongoConnectionDetails(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return DATABASE;
    }

    public String getTable() {
        return TABLE;
    }
}
