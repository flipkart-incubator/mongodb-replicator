package flipkart.mongo.replicator.core.model;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * Created by pradeep on 29/10/14.
 */
public class Cluster {

    public final ImmutableList<ReplicaSetConfig> replicaSets;

    public final ImmutableList<Node> cfgSvrs;

    public Cluster(List<ReplicaSetConfig> replicaSets, List<Node> cfgSvrs) {
        this.replicaSets = ImmutableList.copyOf(replicaSets);
        this.cfgSvrs = ImmutableList.copyOf(cfgSvrs);
    }
}
