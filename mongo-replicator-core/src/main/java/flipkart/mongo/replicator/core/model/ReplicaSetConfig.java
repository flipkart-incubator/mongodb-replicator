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
import com.mongodb.MongoURI;
import flipkart.mongo.replicator.core.exceptions.MongoReplicaSetException;
import flipkart.mongo.replicator.core.exceptions.ReplicatorErrorCode;

import java.util.List;


/**
 * Created by pradeep on 09/10/14.
 */
public class ReplicaSetConfig {

    public final String shardName;
    private List<Node> nodes;

    public ReplicaSetConfig(String shardName, List<Node> nodes) {
        this.shardName = shardName;
        this.nodes = nodes;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public Optional<Node> findNode(String host, int port) {
        for (Node node : nodes) {
            if (node.host.equals(host) && node.port == port) {
                return Optional.of(node);
            }
        }
        return Optional.absent();
    }

    public Optional<Node> getMasterNode() {
        for (Node node : nodes) {
            if (node.getState().equals(NodeState.PRIMARY))
                return Optional.of(node);
        }
        return Optional.absent();
    }

    public MongoURI getMasterClientURI() throws MongoReplicaSetException {

        Optional<Node> masterNode = this.getMasterNode();
        if (masterNode.isPresent())
            return masterNode.get().getMongoURI();

        throw new MongoReplicaSetException(ReplicatorErrorCode.MASTER_NODE_NOT_FOUND);
    }

    @Override
    public int hashCode() {
        return (nodes != null ? nodes.hashCode() : 0);
    }

    @Override
    public String toString() {
        return "ReplicaSetConfig{" +
                "shardName='" + shardName + '\'' +
                ", nodes=" + nodes +
                '}';
    }
}
