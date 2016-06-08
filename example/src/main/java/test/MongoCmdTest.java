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

package test;

import flipkart.mongo.replicator.core.model.Node;
import flipkart.mongodb.replicator.example.TestBuilder;

/**
 * Created by kishan.gajjar on 15/07/15.
 */
public class MongoCmdTest {
    private static final String DB_FOR_DISCOVERY = "admin";

    public static void main(String[] args) {

        TestBuilder testBuilder = new TestBuilder();
        Node node = testBuilder.getMongosNodeFromArgs(args);
//        MongoClient client = MongoConnector.getMongoClient(node.getMongoURI());
//
//        MongoDatabase database = client.getDatabase(DB_FOR_DISCOVERY);
//
//        BasicDBObject replSetGetStatusCommand = new BasicDBObject("replSetGetStatus", 1);
//        Document replSetGetStatus = database.runCommand(replSetGetStatusCommand);
//        System.out.println(" REPLCI STATUS" + replSetGetStatus);
    }
}
