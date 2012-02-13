package balle.strategy.executor.movement;

import balle.controller.Controller;
import balle.strategy.executor.Executor;
import balle.world.FieldObject;
import balle.world.Snapshot;

public interface MovementExecutor extends Executor {

    /**
     * Update the current target we are to approach. This should allow to
     * quickly deal with changing locations of target without the loss of
     * momentum.
     * 
     * @param target
     *            FieldObject of the target we want to approach
     */
    public void updateTarget(FieldObject target);

    @Override
    public boolean isFinished();

    @Override
    public boolean isPossible();

    @Override
    public void updateState(Snapshot snapshot);

    @Override
    public void step(Controller controller);

}
