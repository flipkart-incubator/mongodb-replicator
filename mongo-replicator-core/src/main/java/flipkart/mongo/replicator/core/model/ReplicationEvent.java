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

package flipkart.mongo.replicator.core.model;

import com.google.common.base.Optional;
import com.mongodb.DBObject;
import org.bson.types.BSONTimestamp;

/**
 * Created by pradeep on 10/10/14.
 */
public class ReplicationEvent {
    public final Operation operation;
    public final BSONTimestamp v;
    public final long h;
    public final String namespace;
    public final DBObject eventData;
    public final Optional<DBObject> objectId;

    public ReplicationEvent(String operation, BSONTimestamp v, long h, String namespace, DBObject eventData, DBObject objectId) {
        this.operation = Operation.getOperationType(operation);
        this.v = v;
        this.h = h;
        this.namespace = namespace;
        this.eventData = eventData;
        this.objectId = Optional.fromNullable(objectId);
    }


    @Override
    public String toString() {
        return "ReplicationEvent{" +
                "operation=" + operation +
                ", v=" + v +
                ", h=" + h +
                ", namespace='" + namespace + '\'' +
                ", eventData=" + eventData +
                ", objectId=" + objectId +
                '}';
    }
}
