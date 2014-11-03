package flipkart.mongo.replicator.core.model.bootstrapconfigs;

/**
 * Created by kishan.gajjar on 03/11/14.
 */
public class SchedulerConfigs {

    /**
     * schedulerExecutor will wait for below time before first execution is done
     */
    private long initialDelay = 10;
    /**
     * periodic executions will be done after below delay
     */
    private long periodicDelay = 5;

    public long getInitialDelay() {
        return initialDelay;
    }

    public void setInitialDelay(long initialDelay) {
        this.initialDelay = initialDelay;
    }

    public long getPeriodicDelay() {
        return periodicDelay;
    }

    public void setPeriodicDelay(long periodicDelay) {
        this.periodicDelay = periodicDelay;
    }
}
