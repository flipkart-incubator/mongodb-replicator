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

import com.google.common.base.Function;
import com.google.common.base.Optional;
import flipkart.mongo.replicator.core.interfaces.ICheckPointHandler;
import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;
import flipkart.mongo.replicator.core.manager.ReplicatorManager;
import flipkart.mongo.replicator.core.model.MongoV;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;
import flipkart.mongo.replicator.core.model.ReplicationEvent;
import flipkart.mongo.replicator.core.versions.VersionManager;

/**
 * Created by pradeep on 09/10/14.
 */
public class ReplicaSetManager extends ReplicatorManager {

    private final ReplicaSetConfig rsConfig;
    private Optional<ReplicaSetReplicator> replicaSetReplicator;

    public ReplicaSetManager(ReplicaSetConfig rsConfig, ICheckPointHandler checkPointHandler, IReplicationHandler replicationHandler,
                             MongoV version, Function<ReplicationEvent, Boolean> oplogFilter) {

        super(replicationHandler, VersionManager.singleton().getVersionHandler(version), checkPointHandler, oplogFilter);

        this.rsConfig = rsConfig;
    }

    @Override
    public void startReplicator() {
        replicaSetReplicator = Optional.of(new ReplicaSetReplicator(getTaskContext(), rsConfig));
        replicaSetReplicator.get().doStart();
    }

    @Override
    public void stopReplicator() {
        if (replicaSetReplicator.isPresent())
            replicaSetReplicator.get().doStop();
    }
}
