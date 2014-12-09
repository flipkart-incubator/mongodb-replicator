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

package flipkart.mongo.node.discovery.test.mock;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
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

        DBCursor dbCursor = new DBCursorIterator(MockReplicaSetModel.mockDBObject());
        when(dbCollection.find()).thenReturn(dbCursor);
        return dbCursor;
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
