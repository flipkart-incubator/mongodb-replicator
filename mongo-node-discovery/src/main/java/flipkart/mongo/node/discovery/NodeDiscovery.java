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

import com.google.common.base.Optional;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import flipkart.mongo.node.discovery.exceptions.ConnectionException;
import flipkart.mongo.node.discovery.exceptions.MongoDiscoveryException;
import flipkart.mongo.replicator.core.exceptions.ReplicatorErrorCode;
import flipkart.mongo.replicator.core.model.Node;
import flipkart.mongo.replicator.core.model.NodeState;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kishan.gajjar on 30/10/14.
 */
public class NodeDiscovery {

    private static final Logger logger = LoggerFactory.getLogger(NodeDiscovery.class);
    private ReplicaSetConfig replicaSetConfig;
    private static final String DB_FOR_DISCOVERY = "admin";

    public NodeDiscovery(ReplicaSetConfig replicaSetConfig) {
        this.replicaSetConfig = replicaSetConfig;
    }

    /**
     * will connect to one of the shard in replica set and update and nodes in the replicaSet
     */
    public void discover() throws MongoDiscoveryException {

        Mongo client = null;
        for (Node replicaNode : replicaSetConfig.getNodes()) {
            try {
                client = MongoConnector.getMongoClient(replicaNode.getMongoURI());
                break;
            } catch (ConnectionException e) {
                logger.warn("Not able to connect to replicaNode: " + replicaNode.getMongoURI(), e);
            }
        }

        if (client == null)
            throw new MongoDiscoveryException(ReplicatorErrorCode.NODE_MONGO_CLIENT_FAILURE);

        DB dbConnection = client.getDB(DB_FOR_DISCOVERY);
        CommandResult replSetGetStatus = dbConnection.command("replSetGetStatus");
        List<DBObject> dbDataList = (ArrayList<DBObject>) replSetGetStatus.get("members");
        for (DBObject dbObject : dbDataList) {
            updateReplicaNodeStatus(dbObject, replicaSetConfig);
        }
    }

    private void updateReplicaNodeStatus(DBObject dbObject, ReplicaSetConfig replicaSetConfig) {

        String mongoUri = (String) dbObject.get("name");
        String[] hostData = mongoUri.split(":");
        String host = hostData[0] + ".nm.flipkart.com"; //HACK
        int port = Integer.parseInt(hostData[1]);
        Optional<Node> replicaNode = replicaSetConfig.findNode(host, port);

        if (!replicaNode.isPresent()) {
            logger.error(String.format("ReplicaNode (%s, %s) is not present in replicaSet: %s", host, port, replicaSetConfig));
            return;
        }

        String nodeState = (String) dbObject.get("stateStr");
        if (NodeState.DB_STATE_MAP.containsKey(nodeState)) {
            replicaNode.get().setState(NodeState.DB_STATE_MAP.get(nodeState));
        }
    }
}
