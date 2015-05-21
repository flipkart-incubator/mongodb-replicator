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

package flipkart.mongo.replicator.node.handler;

import com.google.common.base.Optional;
import flipkart.mongo.replicator.core.model.ReplicationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Math.min;

/**
 * Created by kishan.gajjar on 23/11/14.
 */
public class EventReplicaFailureHandler {
    private static final Logger logger = LoggerFactory.getLogger(EventReplicaFailureHandler.class);

    private final int WAIT_TIME_FOR_RETRY = 1000;   // 1000ms
    private final int MAX_WAIT_TIME_FOR_RETRY = 10000;   // 10000ms
    private Optional<ReplicationEvent> replicationEvent = Optional.absent();
    private int retryWaitTime = WAIT_TIME_FOR_RETRY;

    /**
     * this replication failure occurs due to connection timeout in destination dataSource
     * need to retrying after replication after sometime
     * and this sometime should increase exponentially for the same event failure
     * @param failedEvent
     */
    public void handleFailure(ReplicationEvent failedEvent) {

        int sleepTime = this.retryWaitTime;
        if (replicationEvent.isPresent() && replicationEvent.get() == failedEvent) {
            sleepTime += WAIT_TIME_FOR_RETRY;
        }

        this.retryWaitTime = min(sleepTime, MAX_WAIT_TIME_FOR_RETRY);
        this.replicationEvent = Optional.of(failedEvent);

        try {
            logger.info(String.format("Sleeping for replicateEvent: %s.. failure for: %s", this.replicationEvent.get(), this.retryWaitTime));
            Thread.sleep(this.retryWaitTime);
        } catch (InterruptedException e) {
            logger.error("Thread sleep failed", e);
        }
    }
}
