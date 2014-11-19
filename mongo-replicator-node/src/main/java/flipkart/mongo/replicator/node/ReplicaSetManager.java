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

package flipkart.mongo.replicator.node;

import flipkart.mongo.replicator.core.interfaces.ICheckPointHandler;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;

/**
 * Created by pradeep on 09/10/14.
 */
public class ReplicaSetManager {

    private final ReplicaSetConfig rsConfig;
    private final ICheckPointHandler checkPointHandler;

    public ReplicaSetManager(ReplicaSetConfig rsConfig, ICheckPointHandler checkPointHandler) {
        this.rsConfig = rsConfig;
        this.checkPointHandler = checkPointHandler;
    }

    public ReplicaSetConfig getRsConfig() {
        return rsConfig;
    }

    public ICheckPointHandler getCheckPointHandler() {
        return checkPointHandler;
    }

    // Figure out master changes & provide hook for the ReplicaSetReplicator to communicate & act for the changes

}
