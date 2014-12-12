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

package flipkart.mongo.replicator.node.test;

import com.mongodb.*;
import flipkart.mongo.node.discovery.mock.model.MockClusterModel;
import flipkart.mongo.node.discovery.mock.model.MockReplicaSetModel;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;
import junit.framework.TestCase;

/**
 * Created by kishan.gajjar on 12/12/14.
 */
public abstract class BaseReplicatorNodeTest extends TestCase {

    protected ReplicaSetConfig replicaSetConfig;
    protected CommandResult mockCommandResult;
    protected DB mockDB;
    protected Mongo mockMongoClient;
    protected DBCollection mockDBCollection;
    protected DBCursor mockDBCursor;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        mockCommandResult = MockReplicaSetModel.mockMongoResult();
        mockDB = MockReplicaSetModel.mockMongoDB(mockCommandResult);
        mockMongoClient = MockReplicaSetModel.mockMongoClient(mockDB);
        mockDBCollection = MockClusterModel.mockDBCollection(mockDB);
        mockDBCursor = MockClusterModel.mockDBCursor(mockDBCollection);
    }
}
