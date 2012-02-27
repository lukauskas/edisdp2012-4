package balle.strategy.planner;

import balle.controller.Controller;
import balle.main.drawable.Dot;
import balle.strategy.executor.movement.MovementExecutor;
import balle.strategy.executor.turning.RotateToOrientationExecutor;
import balle.world.Coord;
import balle.world.Line;
import balle.world.Orientation;
import balle.world.objects.Point;
import balle.world.objects.StaticFieldObject;

public class GoToFaceBall extends GoToBall {
	
	protected RotateToOrientationExecutor rotateStrategy;

	public GoToFaceBall(MovementExecutor movementExecutor, RotateToOrientationExecutor roe) {
		super(movementExecutor);
		rotateStrategy = roe;
		setOrientation(new Orientation(0));
	}
	
	protected double getReserveDistance() {
		return 0.2;
	}
	
	private Orientation orientation;
	
	public Orientation getOrientation() {
		return orientation;
	}
	
	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
		if (rotateStrategy != null)
			rotateStrategy.setTargetOrientation(orientation);
	}
	
	@Override
	protected StaticFieldObject getTarget() {
		Coord ballPos, desPos;
		ballPos = getSnapshot().getBall().getPosition();
		desPos = new Coord (-getReserveDistance(), 0);
		desPos = desPos.rotate(orientation);
		return new Point(ballPos.add(desPos));
	}
	
	@Override
    public void step(Controller controller) {
        StaticFieldObject target = getTarget();

        if ((getSnapshot() == null) || (getSnapshot().getBalle().getPosition() == null)
                || (target == null))
            return;

        // Update the current state of executor strategy
        executorStrategy.updateState(getSnapshot());
        rotateStrategy.updateState(getSnapshot());

        // If we see the opponent
        if (getSnapshot().getOpponent() != null) {
            Line pathToTarget = new Line(getSnapshot().getBalle().getPosition(),
                    target.getPosition());
            // Check if it is blocking our path
            if (getSnapshot().getOpponent().intersects(pathToTarget)) {
                // pick a new target then
                LOG.info("Opponent is blocking the target, avoiding it");
                target = getAvoidanceTarget();
            }
        }

        // Update the target's location in executorStrategy (e.g. if target
        // moved)
        executorStrategy.updateTarget(target);
        
        // Draw the target
        if (target.getPosition() != null)
            addDrawable(new Dot(target.getPosition(), getTargetColor()));

        // If it says it is not finished, tell it to do something for a step.
        if (!executorStrategy.isFinished()) {
            executorStrategy.step(controller);
        } else if (!rotateStrategy.isFinished()) {
        	executorStrategy.stop(controller);
        	rotateStrategy.step(controller);
        } else {
            // Tell the strategy to stop doing whatever it was doing
            executorStrategy.stop(controller);
            rotateStrategy.stop(controller);
        }
    }

}
