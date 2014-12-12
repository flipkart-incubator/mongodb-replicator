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

package flipkart.mongo.replicator.node.test.mockbuilder;

import com.google.common.base.Function;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoURI;
import flipkart.mongo.node.discovery.mock.model.MockDBObjects;
import flipkart.mongo.replicator.core.interfaces.ICheckPointHandler;
import flipkart.mongo.replicator.core.interfaces.IReplicationEventAdaptor;
import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;
import flipkart.mongo.replicator.core.interfaces.VersionHandler;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;
import flipkart.mongo.replicator.core.model.ReplicationEvent;
import flipkart.mongo.replicator.core.model.TaskContext;
import flipkart.mongo.replicator.core.versions.v2_6.VersionHandler2_6;
import org.mockito.Matchers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by kishan.gajjar on 12/12/14.
 */
public class ReplicationTaskMockBuilder {

    public static ReplicaSetConfig mockReplicaSetWithClient(Mongo mongoClient) {
        ReplicaSetConfig replicaSetConfig = mock(ReplicaSetConfig.class);
        MongoURI mockMongoURI = mock(MongoURI.class);
        try {
            when(replicaSetConfig.getMasterClientURI()).thenReturn(mockMongoURI);
            when(mockMongoURI.connect()).thenReturn(mongoClient);
        } catch (Exception e) {
        }
        return replicaSetConfig;
    }

    public static TaskContext mockTaskContext(MockDBObjects.BSONVariant bsonVariant) {

        ICheckPointHandler checkPointHandler = new TaskContextMockImpl.MockCheckPointHandler();
        IReplicationHandler replicationHandler = new TaskContextMockImpl.MockReplicationHandler(bsonVariant);
        Function<ReplicationEvent, Boolean> oplogFilter = new TaskContextMockImpl.OpLogFilterMock();
        VersionHandler versionHandler = new VersionHandler2_6();

        return new TaskContext.TaskContextBuilder()
                .withCheckPointHandler(checkPointHandler)
                .withOplogFilter(oplogFilter)
                .withReplicationHandler(replicationHandler)
                .withVersionHandler(versionHandler)
                .build();
    }

    public static VersionHandler mockVersionHandler(ReplicationEvent event) {
        VersionHandler versionHandler = new VersionHandler2_6();
        IReplicationEventAdaptor replicationEventAdaptor = mock(IReplicationEventAdaptor.class);
        when(replicationEventAdaptor.convert(Matchers.any(DBObject.class))).thenReturn(event);

        return versionHandler;
    }
}
