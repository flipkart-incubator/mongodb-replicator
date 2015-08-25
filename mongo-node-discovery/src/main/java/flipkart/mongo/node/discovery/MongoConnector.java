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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import flipkart.mongo.replicator.core.model.Node;
import flipkart.mongo.replicator.core.model.bootstrapconfigs.MongoConnectorConfigs;

import java.util.List;
import java.util.Map;

/**
 * Created by kishan.gajjar on 31/10/14.
 */
public class MongoConnector {

    private static Map<Integer, MongoClient> MONGO_CONNECTION_POOL = Maps.newHashMap();

    public static synchronized MongoClient getMongoClient(List<Node> mongoNodes) {

        int nodeHashCode = mongoNodes.hashCode();
        if (MONGO_CONNECTION_POOL.containsKey(nodeHashCode)) {
            return MONGO_CONNECTION_POOL.get(nodeHashCode);
        }

        List<ServerAddress> serverAddresses = Lists.newArrayList();
        for (Node node : mongoNodes) {
            ServerAddress serverAddress = new ServerAddress(node.host, node.port);
            serverAddresses.add(serverAddress);
        }
        MongoConnectorConfigs connectorConfigs = MongoConnectorConfigs.getInstance();
        MongoClient mongoClient = new MongoClient(serverAddresses, connectorConfigs.mongoCredentials);
        MONGO_CONNECTION_POOL.put(nodeHashCode, mongoClient);

        return mongoClient;
    }
}
