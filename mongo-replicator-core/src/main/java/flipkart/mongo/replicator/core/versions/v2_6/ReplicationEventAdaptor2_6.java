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

package flipkart.mongo.replicator.core.versions.v2_6;

import flipkart.mongo.replicator.core.interfaces.IReplicationEventAdaptor;
import flipkart.mongo.replicator.core.model.ReplicationEvent;
import org.bson.BsonTimestamp;
import org.bson.Document;

/**
 * Created by pradeep on 30/10/14.
 */
public class ReplicationEventAdaptor2_6 implements IReplicationEventAdaptor {

    @Override
    public ReplicationEvent convert(Document dbObject) {
        String operation = dbObject.getString("op");
        BsonTimestamp v = (BsonTimestamp) dbObject.get("ts");
        long h = dbObject.getLong("h");
        String namespace = dbObject.getString("ns");
        Document objectId = (Document) dbObject.get("o2");
        Document eventData = (Document) dbObject.get("o");
        return new ReplicationEvent(operation, v, h, namespace, eventData, objectId);
    }
}