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

package flipkart.mongo.node.discovery.test.mock;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import flipkart.mongo.replicator.core.model.Node;

import java.util.List;
import java.util.Map;

/**
 * Created by kishan.gajjar on 09/12/14.
 */
public class MockDBObjects {
    private boolean emptyPrimaryReplica = false;
    private String primaryStateStr = "PRIMARY";

    public MockDBObjects setEmptyPrimaryReplica() {
        this.emptyPrimaryReplica = true;
        return this;
    }

    public MockDBObjects withPrimaryState(String stateStr) {
        this.primaryStateStr = stateStr;
        return this;
    }

    public List<DBObject> mock() {

        List<DBObject> dbObjects = Lists.newArrayList();
        for (Node node : MockReplicaSetModel.MOCK_MONGO_NODES) {

            Map<String, String> dbObjectData = Maps.newHashMap();
            dbObjectData.put("name", String.format("%s:%s", node.host, node.port));

            String hostString = String.format("%s/%s:%s,%s:%s", MockReplicaSetModel.MOCK_SHARD_NAME, node.host, node.port, node.host, node.port);
            if (node.host.equals(MockReplicaSetModel.PRIMARY_NODE_HOST)) {
                dbObjectData.put("stateStr", this.primaryStateStr);
                if (this.emptyPrimaryReplica)
                    hostString = "";
            } else {
                dbObjectData.put("stateStr", "SECONDARY");
            }

            dbObjectData.put("host", hostString);
            dbObjectData.put("_id", MockReplicaSetModel.MOCK_SHARD_NAME);

            dbObjects.add(new BasicDBObject(dbObjectData));
        }

        return dbObjects;
    }

}
