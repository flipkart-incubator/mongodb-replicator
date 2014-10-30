package flipkart.mongo.node.discovery.connector;

/**
 * Created by kishan.gajjar on 13/10/14.
 */
public class MongoConnectionDetails {

    private String serverAddress;
    private int port;
    private String database;
    private String table;

    public MongoConnectionDetails(String serverAddress, int port, String database, String table) {
        this.serverAddress = serverAddress;
        this.port = port;
        this.database = database;
        this.table = table;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getTable() {
        return table;
    }

    public static class ConnectionBuilder {

        private String serverAddress;
        private int port;
        private String database = "config";
        private String table = "shards";

        public ConnectionBuilder(String serverAddress, int port) {
            this.serverAddress = serverAddress;
            this.port = port;
        }

        public void setDatabase(String database) {
            this.database = database;
        }

        public void setTable(String table) {
            this.table = table;
        }

        public MongoConnectionDetails build() {
            return new MongoConnectionDetails(serverAddress, port, database, table);
        }
    }
}
