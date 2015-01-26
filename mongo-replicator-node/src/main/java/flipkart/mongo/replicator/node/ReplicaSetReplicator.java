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

import com.google.common.util.concurrent.AbstractService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import flipkart.mongo.replicator.core.model.ReplicaSetConfig;
import flipkart.mongo.replicator.core.model.TaskContext;
import flipkart.mongo.replicator.core.threadfactory.TaskThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pradeep on 09/10/14.
 */
public class ReplicaSetReplicator extends AbstractService {
    private final String THREAD_NAME_PATTERN = "mongo-replicaset-replicator-%d";

    private final ExecutorService replicatorExecutor;
    private final ReplicationTask.ReplicationTaskFactory replicationTaskFactory;

    public ReplicaSetReplicator(TaskContext taskContext, ReplicaSetConfig rsConfig) {
        ThreadFactoryBuilder threadFactoryBuilder = TaskThreadFactoryBuilder.threadFactoryBuilderInstance(THREAD_NAME_PATTERN);
        this.replicatorExecutor = Executors.newSingleThreadExecutor(threadFactoryBuilder.build());
        this.replicationTaskFactory = new ReplicationTask.ReplicationTaskFactory(taskContext, rsConfig);
    }

    @Override
    protected void doStart() {
        replicatorExecutor.submit(replicationTaskFactory.instance());
    }

    @Override
    protected void doStop() {
        replicatorExecutor.shutdownNow();
    }
}
