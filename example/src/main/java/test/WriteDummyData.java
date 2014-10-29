package test;

import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.UUID;

/**
 * Created by pradeep on 15/10/14.
 */
public class WriteDummyData {

    public static void main(String args[]) throws UnknownHostException {
        MongoClient client = new MongoClient("cart-svc3.nm.flipkart.com:27300");
        DB db = client.getDB("mytest");
        DBCollection collection = db.getCollection("obj");

        for ( int i =0 ;i < 1000; i++) {
            DBObject data = new BasicDBObject();
            data.put("my_id", UUID.randomUUID().toString());
            data.put("ts", System.nanoTime());
            collection.insert(data);
        }

    }
}
