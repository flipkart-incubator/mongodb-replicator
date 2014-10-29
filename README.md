mongodb-replicator
==================

Mongodb replicator WIP

In scope:
- Provide standalone & cluster level replicator
- Only deal with inserts
- Take care of master-slave flips
- Take care of shard additions/removals etc

Scope down:
- Dont deal with updates, only inserts are propagated
- Dont care about chunk migration in cluster mode
