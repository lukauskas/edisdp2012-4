package balle.strategy.executor.movement;

import balle.controller.Controller;
import balle.strategy.ConfusedException;
import balle.strategy.executor.Executor;
import balle.world.Snapshot;
import balle.world.objects.FieldObject;

public interface MovementExecutor extends Executor {

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
    public void updateTarget(FieldObject target);

	@Override
	public boolean isFinished(Snapshot snapshot);

	@Override
	public boolean isPossible(Snapshot snapshot);

	@Override
	public void step(Controller controller, Snapshot snapshot)
			throws ConfusedException;

    public void setStopDistance(double stopDistance);

}
