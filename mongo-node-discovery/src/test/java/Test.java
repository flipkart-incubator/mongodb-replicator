/*
 * Copyright 2012-2015, the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

import com.mongodb.*;

import java.net.UnknownHostException;

/**
 * Created by kishan.gajjar on 11/10/14.
 */
public class Test {

    public static void main(String[] args) throws Exception {

        DBCollection collection = getCollection();
        System.out.println("HERE!" + collection);

        DBCursor dbCursor = collection.find();
        while(dbCursor.hasNext()) {

            DBObject dbObject = dbCursor.next();
            String shardId = (String) dbObject.get("_id");
            String hostString = (String) dbObject.get("host");

            System.out.println("ShardID: " + shardId + " ShardStr: " + hostString);

            String[] hostsInfo = hostString.split(",");
            for (String hostInfo : hostsInfo) {
                String[] data = hostInfo.split("/");
                String hostPortInfo = data[0];
                if (data.length > 1) {
                    hostPortInfo = data[1];
                }
                String[] details = hostPortInfo.split(":");
                String host = details[0];
                String port = details[1];
                System.out.println("DATA:: " + host + " " + port);
            }
        }

//        Map<String, BasicDBObject> rawMap = new HashMap<String, BasicDBObject>((Map)collection.getDB().getStats().get("raw"));
//        System.out.println(rawMap.toString());
//        for (Map.Entry<String, BasicDBObject> dbObject : rawMap.entrySet()) {
//
//            BasicDBObject value = dbObject.getValue();
//            System.out.println("VALUE : " + value.toString());
//        }
        //database.getStats().get("raw")
    }

    private static DBCollection getCollection() {

        MongoClientOptions.Builder clientParametersBuilder = new MongoClientOptions.Builder();
        clientParametersBuilder.autoConnectRetry(true);
        clientParametersBuilder.socketKeepAlive(true);

        try {
//            MongoClient dbClient = new MongoClient(new ServerAddress("localhost", Integer.valueOf("27018")), clientParametersBuilder.build());
            MongoClient dbClient = new MongoClient(new ServerAddress("w3-cart-svc10.nm.flipkart.com", Integer.valueOf("27101")), clientParametersBuilder.build());
            DB database = dbClient.getDB("config");
            return database.getCollection("shards");

        } catch (UnknownHostException ex) {
            throw new RuntimeException(ex);
        }
    }
}
