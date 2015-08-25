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

/**
 * Created by pradeep on 09/10/14.
 */
public class Node {

    public final String host;
    public final int port;
    private NodeState state;

    public Node(String host, int port, NodeState state) {
        this.host = host;
        this.port = port;
        this.state = state;
    }

    public Node(String host, int port) {
        this(host, port, NodeState.UNKNOWN);
    }

    public NodeState getState() {
        return state;
    }

    public void setState(NodeState state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (port != node.port) return false;
        if (host != null ? !host.equals(node.host) : node.host != null) return false;
        if (state != node.state) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = host != null ? host.hashCode() : 0;
        result = 31 * result + port;
        return result;
    }

    @Override
    public String toString() {
        return "Node{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", state=" + state +
                '}';
    }
}
