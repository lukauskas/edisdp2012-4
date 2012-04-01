/**
 * 
 */
package balle.strategy.bezierNav;

import java.awt.Color;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.main.drawable.Dot;
import balle.strategy.FactoryMethod;
import balle.strategy.curve.CustomCHI;
import balle.strategy.executor.movement.OrientedMovementExecutor;
import balle.strategy.pathFinding.SimplePathFinder;
import balle.strategy.planner.AbstractPlanner;
import balle.world.Orientation;
import balle.world.Snapshot;
import balle.world.objects.FieldObject;

/**
 * @author s0909773
 * 
 */
public class GameBezier extends AbstractPlanner {

	protected static final Logger LOG = Logger
			.getLogger(GameBezier.class);

	OrientedMovementExecutor executorStrategy;

    @FactoryMethod(designator = "GameBezier", parameterNames = {})
	public static GameBezier gameBezierFactory() {
		return new GameBezier(new BezierNav(new SimplePathFinder(
				new CustomCHI())));
	}

	public GameBezier(
			OrientedMovementExecutor OrientedMovementExecutor) {
		executorStrategy = OrientedMovementExecutor;
	}

	public OrientedMovementExecutor getExecutorStrategy() {
		return executorStrategy;
	}

	public void setExecutorStrategy(OrientedMovementExecutor executorStrategy) {
		this.executorStrategy = executorStrategy;
	}

    protected FieldObject getTarget(Snapshot snapshot) {
		return snapshot.getBall();
	}

	protected Color getTargetColor() {
		return Color.CYAN;
	}

	@Override
	public void onStep(Controller controller, Snapshot snapshot) {
        FieldObject target = getTarget(snapshot);

		if ((snapshot == null) || (snapshot.getBalle().getPosition() == null)
				|| (target == null))
			return;
		
		Orientation usToTar, midToTar, tarToGoal;
		usToTar = target.getPosition().sub(snapshot.getBalle().getPosition()).getOrientation();
		midToTar = snapshot.getPitch().getPosition().sub(snapshot.getBalle().getPosition()).getOrientation();
		tarToGoal = snapshot.getOpponentsGoal().getPosition()
				.sub(target.getPosition()).getOrientation();
		
		if (midToTar.sub(midToTar).abs().degrees()>90)
			executorStrategy.updateTarget(target, midToTar);
		else 
			executorStrategy.updateTarget(target, usToTar);
			
		
		
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
