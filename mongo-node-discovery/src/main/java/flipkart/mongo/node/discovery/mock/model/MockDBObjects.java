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
import com.google.common.collect.Maps;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import flipkart.mongo.replicator.core.model.Node;
import org.bson.types.BSONTimestamp;

import java.util.List;
import java.util.Map;

/**
 * Created by kishan.gajjar on 09/12/14.
 */
public class MockDBObjects {
    private boolean emptyPrimaryReplica = false;
    private String primaryStateStr = "PRIMARY";
    private BSONVariant bsonVariant = BSONVariant.ACTUAL_TIME;

    public MockDBObjects setEmptyPrimaryReplica() {
        this.emptyPrimaryReplica = true;
        return this;
    }

    public MockDBObjects withPrimaryState(String stateStr) {
        this.primaryStateStr = stateStr;
        return this;
    }

    public MockDBObjects withBSONVariant(BSONVariant bsonVariant) {
        this.bsonVariant = bsonVariant;
        return this;
    }

    public List<DBObject> mock() {

        List<DBObject> dbObjects = Lists.newArrayList();
        for (Node node : MockReplicaSetModel.MOCK_MONGO_NODES) {

            Map<String, Object> dbObjectData = Maps.newHashMap();
            dbObjectData.put("name", String.format("%s:%s", node.host, node.port));

            String hostString = String.format("%s/%s:%s,%s:%s", MockReplicaSetModel.MOCK_SHARD_NAME, node.host, node.port, node.host, node.port);
            if (node.host.equals(MockReplicaSetModel.PRIMARY_NODE_HOST)) {
                dbObjectData.put("stateStr", this.primaryStateStr);
                if (this.emptyPrimaryReplica)
                    hostString = "";
            } else {
                dbObjectData.put("stateStr", "SECONDARY");
            }

            dbObjectData.put("ts", new BSONTimestamp(bsonVariant.getTime(), 1));
            dbObjectData.put("h", 5779775921594602484L);
            dbObjectData.put("v", "2");
            dbObjectData.put("op", "n");
            dbObjectData.put("ns", "");
            dbObjectData.put("o", new BasicDBObject());

            dbObjectData.put("host", hostString);
            dbObjectData.put("_id", MockReplicaSetModel.MOCK_SHARD_NAME);

            dbObjects.add(new BasicDBObject(dbObjectData));
        }

        return dbObjects;
    }

    public static enum BSONVariant {
        INIT(11),
        BEFORE(123),
        ACTUAL_TIME(1234),
        AFTER(12345),
        NOT_VALID(0000),
        THROW_EXCEPTION(-1-1-1);

        private int time;

        BSONVariant(int time) {
            this.time = time;
        }

        public int getTime() {
            return time;
        }
    }
}
