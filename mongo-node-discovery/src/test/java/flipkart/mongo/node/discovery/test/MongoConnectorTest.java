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

package flipkart.mongo.node.discovery.test;

import com.mongodb.MongoClient;
import flipkart.mongo.node.discovery.MongoConnector;
import flipkart.mongo.replicator.core.model.Node;

import static org.mockito.Mockito.mock;

/**
 * Created by kishan.gajjar on 12/12/14.
 */
public class MongoConnectorTest extends BaseDiscoveryTest {

    private Node mockMongoURI;
    private MongoClient mockMongoClient;

    @Override
    public void setUp() throws Exception {
        mockMongoClient = mock(MongoClient.class);
        MongoConnector mongoConnector = new MongoConnector();
    }
}
