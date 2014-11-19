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

import com.google.common.base.Function;
import flipkart.mongo.replicator.core.interfaces.ICheckPointHandler;
import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;
import flipkart.mongo.replicator.core.interfaces.VersionHandler;

/**
 * Created by kishan.gajjar on 02/11/14.
 */

/**
 * contains the common context required by all the ReplicationTask instances
 */
public class TaskContext {

    private VersionHandler versionHandler;
    private Function<ReplicationEvent, Boolean> oplogFilter;
    private IReplicationHandler replicationHandler;
    private ICheckPointHandler checkPointHandler;

    public VersionHandler getVersionHandler() {
        return versionHandler;
    }

    public void setVersionHandler(VersionHandler versionHandler) {
        this.versionHandler = versionHandler;
    }

    public Function<ReplicationEvent, Boolean> getOplogFilter() {
        return oplogFilter;
    }

    public void setOplogFilter(Function<ReplicationEvent, Boolean> oplogFilter) {
        this.oplogFilter = oplogFilter;
    }

    public IReplicationHandler getReplicationHandler() {
        return replicationHandler;
    }

    public void setReplicationHandler(IReplicationHandler replicationHandler) {
        this.replicationHandler = replicationHandler;
    }

    public ICheckPointHandler getCheckPointHandler() {
        return checkPointHandler;
    }

    public void setCheckPointHandler(ICheckPointHandler checkPointHandler) {
        this.checkPointHandler = checkPointHandler;
    }
}
