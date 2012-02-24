package balle.strategy;

import balle.controller.Controller;
import balle.world.Snapshot;

public interface Strategy {

    /**
     * Notify the strategy of a change in the current state
     * 
     * @param snapshot
     */
    public void updateState(Snapshot snapshot);

    /**
     * Tell the strategy to do a step (e.g. move forward).
     */
    public void step(Controller controller);

    /**
     * Tell the strategy to stop doing whatever it was doing.
     * 
     * @param controller
     */
    public void stop(Controller controller);

}
