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

package flipkart.mongo.node.discovery.mock.model;

import com.mongodb.*;
import org.mockito.Matchers;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by kishan.gajjar on 09/12/14.
 */
public class MockClusterModel {

    public static DBCollection mockDBCollection(DB mongoDB) {

        DBCollection dbCollection = mock(DBCollection.class);
        when(mongoDB.getCollection(Matchers.anyString())).thenReturn(dbCollection);
        return dbCollection;
    }

    public static DBCursor mockDBCursor(DBCollection dbCollection) {
        return mockDBCursorWithDBObject(dbCollection, new MockDBObjects());
    }

    public static DBCursor mockDBCursorWithDBObject(DBCollection dbCollection, MockDBObjects mockDBObjects) {

        DBCursor dbCursor = new DBCursorIterator(mockDBObjects.mock());
        when(dbCollection.find()).thenReturn(dbCursor);
        when(dbCollection.find(Matchers.any(BasicDBObject.class))).thenReturn(dbCursor);

        return dbCursor;
    }

    public static void mockForUnknownState(CommandResult commandResult) {
       when(commandResult.get("members")).thenReturn(new MockDBObjects().withPrimaryState("UNKNOWN").mock());
    }

    public static void withShardName(CommandResult commandResult) {
        when(commandResult.get("set")).thenReturn(MockReplicaSetModel.MOCK_SHARD_NAME);
    }

    public static class DBCursorIterator extends DBCursor {

        private List<DBObject> dbObjects;
        private int cursor = 0;

        public DBCursorIterator(List<DBObject> dbObjects) {
            super(mock(DBCollection.class), null, null, null);
            this.dbObjects = dbObjects;
        }

        @Override
        public boolean hasNext() {
            return dbObjects.size() > cursor;
        }

        @Override
        public DBObject next() {
            DBObject dbObject = dbObjects.get(cursor);
            cursor++;
            return dbObject;
        }

        @Override
        public void remove() {

        }
    }
}
