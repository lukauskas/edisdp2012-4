/**
 * 
 */
package balle.strategy.planner;

import java.awt.Color;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.main.drawable.Dot;
import balle.strategy.executor.movement.OrientedMovementExecutor;
import balle.world.Snapshot;
import balle.world.objects.StaticFieldObject;

/**
 * @author s0909773
 * 
 */
public class SimpleGoToBallFaceGoal extends AbstractPlanner {

	protected static final Logger LOG = Logger
			.getLogger(SimpleGoToBallFaceGoal.class);

	OrientedMovementExecutor executorStrategy;

	public SimpleGoToBallFaceGoal(
			OrientedMovementExecutor OrientedMovementExecutor) {
		executorStrategy = OrientedMovementExecutor;
	}

	public OrientedMovementExecutor getExecutorStrategy() {
		return executorStrategy;
	}

	public void setExecutorStrategy(OrientedMovementExecutor executorStrategy) {
		this.executorStrategy = executorStrategy;
	}

	protected StaticFieldObject getTarget(Snapshot snapshot) {
		return snapshot.getBall();
	}

	protected Color getTargetColor() {
		return Color.CYAN;
	}

	@Override
	public void onStep(Controller controller, Snapshot snapshot) {
		StaticFieldObject target = getTarget(snapshot);

		if ((snapshot == null) || (snapshot.getBalle().getPosition() == null)
				|| (target == null))
			return;

		// Update the target's location in executorStrategy (e.g. if target
		// moved)
		executorStrategy.updateTarget(target, snapshot.getOpponentsGoal()
				.getPosition().sub(target.getPosition()).getOrientation());
		// Draw the target
		addDrawables(executorStrategy.getDrawables());
		if (target.getPosition() != null)
			addDrawable(new Dot(target.getPosition(), getTargetColor()));

		// If it says it is not finished, tell it to do something for a step.
		if (!executorStrategy.isFinished(snapshot)) {
			executorStrategy.step(controller, snapshot);
		} else {
			// Tell the strategy to stop doing whatever it was doing
			executorStrategy.stop(controller);
		}
	}

	@Override
	public void stop(Controller controller) {
		executorStrategy.stop(controller);
	}
}
