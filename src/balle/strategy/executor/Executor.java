package balle.strategy.executor;

import balle.strategy.Strategy;
import balle.world.Snapshot;

public interface Executor extends Strategy {

    /**
     * Is the executor finished executing the task given;
     * @param snapshot TODO
     */
    public boolean isFinished(Snapshot snapshot);

    /**
     * Returns whether the executor believes the task is still possible to
     * achieve;
     * @param snapshot TODO
     */
    public boolean isPossible(Snapshot snapshot);

}
