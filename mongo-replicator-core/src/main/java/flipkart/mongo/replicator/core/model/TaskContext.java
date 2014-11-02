package flipkart.mongo.replicator.core.model;

import com.google.common.base.Function;
import flipkart.mongo.replicator.core.interfaces.ICheckPointHandler;
import flipkart.mongo.replicator.core.interfaces.IReplicationHandler;
import flipkart.mongo.replicator.core.interfaces.VersionHandler;

/**
 * Created by kishan.gajjar on 02/11/14.
 */

/**
 * contains the common context required by all the ReplicationTask instances
 */
public class TaskContext {

    private VersionHandler versionHandler;
    private Function<ReplicationEvent, Boolean> oplogFilter;
    private IReplicationHandler replicationHandler;
    private ICheckPointHandler checkPointHandler;

    public VersionHandler getVersionHandler() {
        return versionHandler;
    }

    public void setVersionHandler(VersionHandler versionHandler) {
        this.versionHandler = versionHandler;
    }

    public Function<ReplicationEvent, Boolean> getOplogFilter() {
        return oplogFilter;
    }

    public void setOplogFilter(Function<ReplicationEvent, Boolean> oplogFilter) {
        this.oplogFilter = oplogFilter;
    }

    public IReplicationHandler getReplicationHandler() {
        return replicationHandler;
    }

    public void setReplicationHandler(IReplicationHandler replicationHandler) {
        this.replicationHandler = replicationHandler;
    }

    public ICheckPointHandler getCheckPointHandler() {
        return checkPointHandler;
    }

    public void setCheckPointHandler(ICheckPointHandler checkPointHandler) {
        this.checkPointHandler = checkPointHandler;
    }
}
