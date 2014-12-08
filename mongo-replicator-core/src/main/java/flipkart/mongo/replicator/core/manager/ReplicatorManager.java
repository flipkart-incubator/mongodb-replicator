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

package flipkart.mongo.replicator.core.manager;

import com.google.common.base.Function;
import flipkart.mongo.replicator.core.interfaces.ICheckPointHandler;
import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;
import flipkart.mongo.replicator.core.interfaces.VersionHandler;
import flipkart.mongo.replicator.core.model.ReplicationEvent;
import flipkart.mongo.replicator.core.model.TaskContext;

/**
 * Created by kishan.gajjar on 03/12/14.
 */
public abstract class ReplicatorManager {

    protected final IReplicationHandler replicationHandler;
    protected final VersionHandler versionHandler;
    protected final ICheckPointHandler checkPointHandler;
    protected final Function<ReplicationEvent, Boolean> oplogFilter;

    public ReplicatorManager(IReplicationHandler replicationHandler, VersionHandler versionHandler,
                             ICheckPointHandler checkPointHandler, Function<ReplicationEvent, Boolean> oplogFilter) {

        this.replicationHandler = replicationHandler;
        this.versionHandler = versionHandler;
        this.checkPointHandler = checkPointHandler;
        this.oplogFilter = oplogFilter;
    }

    protected TaskContext getTaskContext() {

        TaskContext context = new TaskContext();
        context.setCheckPointHandler(checkPointHandler);
        context.setOplogFilter(oplogFilter);
        context.setReplicationHandler(replicationHandler);
        context.setVersionHandler(versionHandler);

        return context;
    }

    public abstract void startReplicator();
    public abstract void stopReplicator();
}
