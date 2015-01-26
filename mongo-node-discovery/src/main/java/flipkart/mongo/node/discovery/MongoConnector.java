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

package flipkart.mongo.node.discovery;

import com.google.common.collect.Maps;
import com.mongodb.Mongo;
import com.mongodb.MongoURI;
import flipkart.mongo.node.discovery.exceptions.ConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by kishan.gajjar on 31/10/14.
 */
public class MongoConnector {

    private static final Logger logger = LoggerFactory.getLogger(MongoConnector.class);
    private static Map<String, Mongo> MONGO_CONNECTION_POOL = Maps.newHashMap();

    public static Mongo getMongoClient(MongoURI mongoURI) throws ConnectionException {

        String mongoUriString = mongoURI.toString();
        if (MONGO_CONNECTION_POOL.containsKey(mongoUriString)) {
            return MONGO_CONNECTION_POOL.get(mongoUriString);
        }

        try {
            Mongo mongoClient = mongoURI.connect();
            MONGO_CONNECTION_POOL.put(mongoUriString, mongoClient);
            return mongoClient;
        } catch (Exception e) {
            logger.error("Not able to connect configSvr: " + mongoUriString, e);
        }

        throw new ConnectionException("Not able to connect to MongoUri: " + mongoUriString);
    }
}
