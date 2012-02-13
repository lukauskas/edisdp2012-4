package balle.strategy.executor;

import balle.controller.Controller;
import balle.world.Snapshot;

public interface Executor {

    /**
     * Is the executor finished executing the task given;
     */
    public boolean isFinished();

    /**
     * Returns whether the executor believes the task is still possible to
     * achieve;
     */
    public boolean isPossible();

    /**
     * Notify the executor of a change in the current state
     * 
     * @param snapshot
     */
    public void updateState(Snapshot snapshot);

    /**
     * Tell the executor to do a step (e.g. move forward).
     */
    public void step(Controller controller);

    /**
     * Tell the executor to stop doing whatever it was doing.
     * 
     * @param controller
     */
    public void stop(Controller controller);

}
