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
import com.google.common.base.Strings;
import com.mongodb.MongoURI;

/**
 * Created by pradeep on 09/10/14.
 */
public class Node {

    public final String host;
    public final int port;
    private Optional<Authorization> optionalAuthorization = Optional.absent();
    private NodeState state;

    public Node(String host, int port, NodeState state) {
        this.host = host;
        this.port = port;
        this.state = state;
    }

    public Node(String host, int port) {
        this(host, port, NodeState.UNKNOWN);
    }

    public MongoURI getMongoURI() {
        String hostURI = String.format("%s:%s", host, port);
        String authorizationURI = null;
        String authorizedDB = "";
        if (optionalAuthorization.isPresent()) {
            Authorization authorization = optionalAuthorization.get();
            authorizationURI = String.format("%s:%s", authorization.username, authorization.password);
            authorizedDB = authorization.getAuthorizedDB().or(authorizedDB);
        }

        String mongoURI;
        if (!Strings.isNullOrEmpty(authorizationURI)) {
            mongoURI = String.format("%s@%s/%s", authorizationURI, hostURI, authorizedDB);
        } else {
            mongoURI = hostURI;
        }
        return new MongoURI("mongodb://" + mongoURI);
    }

    public NodeState getState() {
        return state;
    }

    public void setState(NodeState state) {
        this.state = state;
    }

    public void setAuthorization(Authorization optionalAuthorization) {
        this.optionalAuthorization = Optional.fromNullable(optionalAuthorization);
    }

    public Optional<Authorization> getAuthorization() {
        return optionalAuthorization;
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
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Node{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", optionalAuthorization=" + optionalAuthorization +
                ", state=" + state +
                '}';
    }
}
