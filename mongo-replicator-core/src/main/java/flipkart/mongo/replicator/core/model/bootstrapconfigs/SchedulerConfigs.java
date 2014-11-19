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

package flipkart.mongo.replicator.core.model.bootstrapconfigs;

/**
 * Created by kishan.gajjar on 03/11/14.
 */
public class SchedulerConfigs {

    /**
     * schedulerExecutor will wait for below time before first execution is done
     * TimeUnit is in seconds
     */
    private long initialDelay = 10;
    /**
     * periodic executions will be done after below delay
     * TimeUnit is in seconds
     */
    private long periodicDelay = 5;

    public long getInitialDelay() {
        return initialDelay;
    }

    public void setInitialDelay(long initialDelay) {
        this.initialDelay = initialDelay;
    }

    public long getPeriodicDelay() {
        return periodicDelay;
    }

    public void setPeriodicDelay(long periodicDelay) {
        this.periodicDelay = periodicDelay;
    }
}
