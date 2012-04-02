package balle.strategy.planner;

import balle.controller.Controller;
import balle.main.drawable.Dot;
import balle.strategy.ConfusedException;
import balle.strategy.executor.movement.MovementExecutor;
import balle.strategy.executor.turning.RotateToOrientationExecutor;
import balle.world.Coord;
import balle.world.Line;
import balle.world.Orientation;
import balle.world.Snapshot;
import balle.world.objects.FieldObject;
import balle.world.objects.Point;

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
    protected FieldObject getTarget(Snapshot snapshot) {
		Coord ballPos, desPos;
		ballPos = snapshot.getBall().getPosition();
		desPos = new Coord (-getReserveDistance(), 0);
		desPos = desPos.rotate(orientation);
		return new Point(ballPos.add(desPos));
	}
	
	@Override
    public void onStep(Controller controller, Snapshot snapshot) throws ConfusedException {
        FieldObject target = getTarget(snapshot);

		if ((snapshot == null)
				|| (snapshot.getBalle().getPosition() == null)
                || (target == null))
            return;

        // If we see the opponent
		if (snapshot.getOpponent() != null) {
			Line pathToTarget = new Line(snapshot.getBalle().getPosition(),
                    target.getPosition());
            // Check if it is blocking our path
			if (snapshot.getOpponent().intersects(pathToTarget)) {
                // pick a new target then
                LOG.info("Opponent is blocking the target, avoiding it");
				target = getAvoidanceTarget(snapshot);
            }
        }

        // Update the target's location in executorStrategy (e.g. if target
        // moved)
        executorStrategy.updateTarget(target);
        
        // Draw the target
        if (target.getPosition() != null)
            addDrawable(new Dot(target.getPosition(), getTargetColor()));

        // If it says it is not finished, tell it to do something for a step.
        if (!executorStrategy.isFinished(snapshot)) {
            executorStrategy.step(controller, snapshot);

        } else if (!rotateStrategy.isFinished(snapshot)) {
        	executorStrategy.stop(controller);
            rotateStrategy.step(controller, snapshot);
        } else {
            // Tell the strategy to stop doing whatever it was doing
            executorStrategy.stop(controller);
            rotateStrategy.stop(controller);
        }
    }

}
