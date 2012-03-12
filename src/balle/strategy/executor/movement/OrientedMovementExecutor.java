package balle.strategy.executor.movement;

import balle.strategy.executor.Executor;
import balle.world.Orientation;
import balle.world.objects.FieldObject;

public interface OrientedMovementExecutor extends Executor {

	    /**
     * Update the current target we are to approach. This should allow to
     * quickly deal with changing locations of target without the loss of
     * momentum.
     * 
     * If fieldObject
     * 
     * @param target
     *            FieldObject of the target we want to approach
     */
    public void updateTarget(FieldObject target, Orientation orientation);

	public void setStopDistance(double stopDistance);

}
