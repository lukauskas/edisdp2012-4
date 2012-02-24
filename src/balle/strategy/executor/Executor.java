package balle.strategy.executor;

import balle.strategy.Strategy;

public interface Executor extends Strategy {

    /**
     * Is the executor finished executing the task given;
     */
    public boolean isFinished();

    /**
     * Returns whether the executor believes the task is still possible to
     * achieve;
     */
    public boolean isPossible();

}
