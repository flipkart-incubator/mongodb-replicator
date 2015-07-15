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

import com.mongodb.DBObject;
import flipkart.mongo.replicator.core.interfaces.IReplicationEventAdaptor;
import flipkart.mongo.replicator.core.model.ReplicationEvent;
import org.bson.types.BSONTimestamp;

/**
 * Created by pradeep on 30/10/14.
 */
public class ReplicationEventAdaptor2_6 implements IReplicationEventAdaptor {

    @Override
    public ReplicationEvent convert(DBObject dbObject) {
        String operation = dbObject.get("op").toString();
        BSONTimestamp v = (BSONTimestamp) dbObject.get("ts");
        Long h = (Long) dbObject.get("h");
        String namespace = dbObject.get("ns").toString();
        DBObject objectId = (DBObject) dbObject.get("o2");
        return new ReplicationEvent(operation, v, h, namespace, (DBObject) dbObject.get("o"), objectId);
    }
}