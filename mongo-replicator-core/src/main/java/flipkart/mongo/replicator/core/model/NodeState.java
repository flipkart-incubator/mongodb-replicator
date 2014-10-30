package flipkart.mongo.replicator.core.model;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Created by pradeep on 29/10/14.
 */
public enum NodeState {
    PRIMARY, SECONDARY, UNKNOWN;

    public static Map<String, NodeState> DB_STATE_MAP = ImmutableMap.of(
            "PRIMARY", NodeState.PRIMARY,
            "SECONDARY", NodeState.SECONDARY
    );
}
