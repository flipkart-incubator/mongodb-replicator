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
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import flipkart.mongo.node.discovery.exceptions.MongoDiscoveryException;
import flipkart.mongo.replicator.core.model.Node;
import flipkart.mongo.replicator.core.model.NodeState;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by kishan.gajjar on 30/10/14.
 */
public class NodeDiscovery {

    private static final Logger logger = LoggerFactory.getLogger(NodeDiscovery.class);
    private ReplicaSetConfig mongoReplicaSet;
    private static final String DB_FOR_DISCOVERY = "admin";

    public NodeDiscovery(ReplicaSetConfig replicaSetConfig) {
        this.mongoReplicaSet = replicaSetConfig;
    }

    /**
     * will connect to one of the shard in replica set and update and nodes in the replicaSet
     */
    public ReplicaSetConfig discover() throws MongoDiscoveryException {

        List<Node> nodesInReplicaSet = Lists.newArrayList();
        MongoClient client = MongoConnector.getMongoClient(mongoReplicaSet.getNodes());

        MongoDatabase dbConnection = client.getDatabase(DB_FOR_DISCOVERY);
        Document replicaSetCmd = new Document("replSetGetStatus", "1");
        Document replSetGetStatus = dbConnection.runCommand(replicaSetCmd);
        List<Document> memberDocuments = (List<Document>) replSetGetStatus.get("members");

        for (Document member : memberDocuments) {
            Node replicaNodeWithState = getReplicaNodeWithState(member, mongoReplicaSet);
            nodesInReplicaSet.add(replicaNodeWithState);
        }

        ReplicaSetConfig replicaSetConfig = new ReplicaSetConfig(getShardName(replSetGetStatus), nodesInReplicaSet);
        logger.info("ReplicaSet found: " + replicaSetConfig);
        return replicaSetConfig;
    }

    private String getShardName(Document replSetGetStatus) {

        String shardName = (String) replSetGetStatus.get("set");
        if (Strings.isNullOrEmpty(shardName)) {
            shardName = mongoReplicaSet.shardName;
        }

        return shardName;
    }

    private Node getReplicaNodeWithState(Document member, ReplicaSetConfig replicaSetConfig) {

        String mongoUri = member.getString("name");
        String[] hostData = mongoUri.split(":");
        String host = hostData[0];
        int port = Integer.parseInt(hostData[1]);
        Optional<Node> replicaNodeOptional = replicaSetConfig.findNode(host, port);

        if (!replicaNodeOptional.isPresent()) {
            logger.error(String.format("ReplicaNode (%s, %s) is not present in replicaSet: %s", host, port, replicaSetConfig));
            Node node = new Node(host, port);
            replicaNodeOptional = Optional.of(node);
        }

        Node replicaNode = replicaNodeOptional.get();
        String nodeState = member.getString("stateStr");
        if (NodeState.DB_STATE_MAP.containsKey(nodeState)) {
            replicaNode.setState(NodeState.DB_STATE_MAP.get(nodeState));
        }

        return replicaNode;
    }
}
