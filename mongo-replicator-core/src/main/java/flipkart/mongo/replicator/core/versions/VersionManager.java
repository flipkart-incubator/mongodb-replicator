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

package flipkart.mongo.replicator.core.versions;

import com.google.common.collect.ImmutableList;
import flipkart.mongo.replicator.core.interfaces.VersionHandler;
import flipkart.mongo.replicator.core.model.MongoV;
import org.reflections.Reflections;

import java.util.Set;

/**
 * Created by pradeep on 30/10/14.
 */
public class VersionManager {

    protected final  ImmutableList<VersionHandler> versionHandlers;

    protected VersionManager() {
        Reflections reflections = new Reflections("flipkart.mongo.replicator.core.versions");
        Set<Class<? extends VersionHandler>> classes = reflections.getSubTypesOf(VersionHandler.class);
        ImmutableList.Builder<VersionHandler> builder = new ImmutableList.Builder<VersionHandler>();

        for ( Class<? extends VersionHandler> clazz : classes) {
            try {
                builder.add(clazz.newInstance());
            } catch (Exception e) { }
        }

        versionHandlers = builder.build();
    }

    private static Integer mutex = new Integer(0);
    private static VersionManager instance = null;

    public static VersionManager singleton() {
        if ( instance == null) {
            synchronized (mutex) {
                if ( instance == null) {
                    instance = new VersionManager();
                }
            }
        }

        return instance;
    }

    public VersionHandler getVersionHandler(MongoV version ) {
        for ( VersionHandler versionHandler : versionHandlers ) {
            if ( versionHandler.getFrom().isGte(version) && versionHandler.getTo().isLte(version) ) {
                return versionHandler;
            }
        }

        return null;
    }

}
