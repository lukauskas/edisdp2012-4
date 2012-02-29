package balle.strategy.executor.movement;

import balle.controller.Controller;
import balle.strategy.executor.Executor;
import balle.world.Orientation;
import balle.world.Snapshot;
import balle.world.objects.StaticFieldObject;

public interface OrientedMovementExecutor extends Executor {

	/**
	 * Update the current target we are to approach. This should allow to
	 * quickly deal with changing locations of target without the loss of
	 * momentum.
	 * 
	 * If fieldObject
	 * 
	 * @param target
	 *            StaticFieldObject of the target we want to approach
	 */
	public void updateTarget(StaticFieldObject target, Orientation orientation);

	@Override
	public boolean isFinished();

	@Override
	public boolean isPossible();

	@Override
	public void updateState(Snapshot snapshot);

	@Override
	public void step(Controller controller);

	public void setStopDistance(double stopDistance);

}
