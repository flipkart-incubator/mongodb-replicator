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

package flipkart.mongo.node.discovery.mock.model;

import com.google.common.collect.Lists;
import com.mongodb.*;
import flipkart.mongo.node.discovery.MongoConnector;
import flipkart.mongo.replicator.core.model.Node;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by kishan.gajjar on 09/12/14.
 */
public class MockReplicaSetModel {

    public static final String PRIMARY_NODE_HOST = "test1";
    public static String MOCK_SHARD_NAME = "mockShardName";
    public static final List<Node> MOCK_MONGO_NODES = Lists.newArrayList(
            new Node(PRIMARY_NODE_HOST, 1111),
            new Node("test2", 2222),
            new Node("test3", 3333)
    );

    public static CommandResult mockMongoResult() {
        CommandResult commandResult = mock(CommandResult.class);
        when(commandResult.get("members")).thenReturn(new MockDBObjects().mock());
        return commandResult;
    }

    public static void mockWithEmptyHostState(DBCollection dbCollection) {
        List<DBObject> dbObjects = new MockDBObjects().setEmptyPrimaryReplica().mock();
        DBCursor dbCursor = new MockClusterModel.DBCursorIterator(dbObjects);
        when(dbCollection.find()).thenReturn(dbCursor);
    }

    public static DB mockMongoDB(CommandResult commandResult) {
        DB db = mock(DB.class);
        when(db.command(Matchers.anyString())).thenReturn(commandResult);
        return db;
    }

    public static Mongo mockMongoClient(DB mongoDB) {
        Mongo mongoClient = mock(Mongo.class);
        when(mongoClient.getDB(Matchers.anyString())).thenReturn(mongoDB);
        return mongoClient;
    }

    public static Map<String, Mongo> mockMongoConnectionPool(Mongo mongoClient) throws IllegalAccessException {

        Map<String, Mongo> connectionPool = mock(Map.class);
        Field field = PowerMockito.field(MongoConnector.class, "MONGO_CONNECTION_POOL");
        field.set(MongoConnector.class, connectionPool);
        when(connectionPool.containsKey(Matchers.anyString())).thenReturn(true);
        when(connectionPool.get(Matchers.anyString())).thenReturn(mongoClient);

        return connectionPool;
    }

    public static List<ReplicaSetConfig> mockReplicaSetConfigs(int replicaSetSize, int replicaNodeSize) {
        List<ReplicaSetConfig> replicaSetConfigs = Lists.newArrayList();

        for (int replicaSize = 0; replicaSize < replicaSetSize; replicaSize++) {
            List<Node> mockReplicaNodes = MOCK_MONGO_NODES.subList(0, replicaNodeSize);
            ReplicaSetConfig replicaSetConfig = new ReplicaSetConfig(MOCK_SHARD_NAME, mockReplicaNodes);
            replicaSetConfigs.add(replicaSetConfig);
        }

        return replicaSetConfigs;
    }
}
