package balle.strategy.planner;

import balle.controller.Controller;
import balle.strategy.Strategy;
import balle.world.Snapshot;

public abstract class AbstractPlanner implements Strategy {
    private Snapshot snapshot;

    /**
     * Notify the executor of a change in the current state
     * 
     * @Override
     * @param snapshot
     */
    @Override
    public void updateState(Snapshot snapshot) {
        this.snapshot = snapshot;
    }

    /**
     * Returns the current snapshot
     * 
     * @return the snapshot
     */
    protected Snapshot getSnapshot() {
        return snapshot;
    }

    @Override
    public void stop(Controller controller) {
        controller.stop();
    }
}
