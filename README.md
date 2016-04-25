# mongodb-replicator


Mongodb replicator is a java based library to stream changes (change-data-capture aka CDC) from MongoDB database and provides an application hook for the developers to Replication Handlers for custom transformation & specific destination related implementation. Sample implementation is [here](https://github.com/flipkart-incubator/mongodb-replicator/blob/master/example/src/main/java/flipkart/mongodb/replicator/example/ReplicationHandlerExample.java).

##### Features:

- Stream ordered change-list entries from master oplog
- Works with both non-sharded & sharded MongoDB clusters 
- Takes care of master-slave flips & listens to master always
- Takes care of shard additions/removals in a sharded setup
- Express filters as your interested data set and only those filtered events will reach ReplicationHandler
- Provides check-point hooks which can be implemented based on the needed 

##### Known Limitations:
- The change-list entries across shards in a sharded-cluster can have data ordering issues if chunk migration is turned on. In a sharded-cluster oplog streaming during chunk migration is not synchronized. However, this can be worked around by assuming partial ordering guarantees from this stream & have another level of ordering (using entity versions). 

##### Use-cases:
- Can be used as streaming pipe for Hot-Cold store implementation where cold has to be eventually consistent & Hot is MongoDB
- Can be used as sourcing process for change propogation
- Can be used for low-latency event sourcings & further processing
- Can be used also for XDCR (cross-dc-replication)

