package flipkart.mongo.node.discovery;

import com.google.common.collect.Maps;
import com.mongodb.Mongo;
import com.mongodb.MongoURI;
import flipkart.mongo.node.discovery.exceptions.ConnectionException;

import java.net.UnknownHostException;
import java.util.Map;

/**
 * Created by kishan.gajjar on 31/10/14.
 */
public class MongoConnector {

    private static Map<String, Mongo> MONGO_CONNECTION_POOL = Maps.newHashMap();

    public static Mongo getMongoClient(MongoURI mongoURI) throws ConnectionException {

        String mongoUriString = mongoURI.toString();
        if (MONGO_CONNECTION_POOL.containsKey(mongoUriString)) {
            System.out.println("GOT from connection pool: " + mongoUriString);
            return MONGO_CONNECTION_POOL.get(mongoUriString);
        }

        try {
            Mongo mongoClient = mongoURI.connect();
            MONGO_CONNECTION_POOL.put(mongoUriString, mongoClient);
            System.out.println("Added connection pool: " + mongoUriString);
            return mongoClient;
        } catch (UnknownHostException e) {
            System.out.println("Not able to connect configSvr: " + mongoUriString);
            e.printStackTrace();
        }

        throw new ConnectionException("Not able to connect to MongoUri: " + mongoUriString);
    }
}
