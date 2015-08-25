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

import com.google.common.collect.ImmutableList;
import com.mongodb.MongoCredential;

import java.util.List;

/**
 * Created by kishan.gajjar on 25/08/15.
 */
public class MongoConnectorConfigs {
    public final ImmutableList<MongoCredential> mongoCredentials;
    private static MongoConnectorConfigs INSTANCE;

    private MongoConnectorConfigs(List<MongoCredential> mongoCredentials) {
        this.mongoCredentials = ImmutableList.copyOf(mongoCredentials);
    }

    public static void setInstance(List<MongoCredential> mongoCredentials) {
        MongoConnectorConfigs mongoConnectorConfigs = new MongoConnectorConfigs(mongoCredentials);
        INSTANCE = mongoConnectorConfigs;
    }

    public static MongoConnectorConfigs getInstance() {
        if (INSTANCE == null)
            throw new NullPointerException("MongoConnectorConfig is null");
        return INSTANCE;
    }

    @Override
    public int hashCode() {
        return mongoCredentials != null ? mongoCredentials.hashCode() : 0;
    }
}
