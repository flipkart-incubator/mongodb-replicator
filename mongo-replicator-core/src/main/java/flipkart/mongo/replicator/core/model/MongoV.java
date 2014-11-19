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

/**
 * Created by pradeep on 17/10/14.
 */
public class MongoV {
    public final int major;
    public final int minor;
    public final Optional<Integer> patch;

    public MongoV(int major, int minor, int patch) {
        this(major, minor, Optional.of(patch));
    }

    protected MongoV(int major, int minor, Optional<Integer> patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public MongoV(int major, int minor) {
        this(major, minor, Optional.<Integer>absent());
    }


    public boolean isGte(MongoV v) {
        return (major >= v.major) ||
                (major == v.major && minor >= v.minor) ||
                (major == v.major && minor == v.minor && ( (patch.isPresent() && v.patch.isPresent()) && patch.get() >= v.patch.get() ));
    }

    public boolean isLte(MongoV v) {
        return (major <= v.major) ||
                (major == v.major && minor <= v.minor) ||
                (major == v.major && minor == v.minor && ( (patch.isPresent() && v.patch.isPresent()) && patch.get() <= v.patch.get() ));
    }
}
