package balle.strategy.executor.movement;

import java.util.ArrayList;

import balle.controller.Controller;
import balle.main.drawable.Drawable;
import balle.strategy.executor.turning.RotateToOrientationExecutor;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Snapshot;
import balle.world.objects.FieldObject;
import balle.world.objects.Robot;

public class GoToObject implements MovementExecutor {

	private double stopDistance = 0.2;

	private final static double EPSILON = 0.00001;
	private final static double DISTANCE_DIFF_TO_TURN_FOR = 0.3;

	public final static int DEFAULT_MOVEMENT_SPEED = 500;

    protected FieldObject       target                    = null;
	private boolean isMoving = false;

	private int movementSpeed = DEFAULT_MOVEMENT_SPEED;

	RotateToOrientationExecutor turningExecutor = null;

	public GoToObject(RotateToOrientationExecutor turningExecutor) {
		this.turningExecutor = turningExecutor;
	}

	@Override
    public void updateTarget(FieldObject target) {
		this.target = target;
	}

	@Override
	public boolean isFinished(Snapshot snapshot) {
		Robot robot = snapshot.getBalle();
		Coord currentPosition = robot.getPosition();
		if ((target == null) || (currentPosition == null)) {
			return false;
		}
		return ((currentPosition.dist(target.getPosition()) - stopDistance) < EPSILON);
	}

	@Override
	public boolean isPossible(Snapshot snapshot) {
		if (turningExecutor == null)
			return false;

		Robot robot = snapshot.getBalle();
		Coord currentPosition = robot.getPosition();
		Orientation currentOrientation = robot.getOrientation();
		Coord targetPosition = (target != null) ? target.getPosition() : null;
		return ((currentOrientation != null) && (currentPosition != null) && (targetPosition != null));
	}

	@Override
	public void step(Controller controller, Snapshot snapshot) {
		// Fail quickly if state not set
		if (snapshot == null)
			return;

		Coord targetCoord = target.getPosition();
		Robot robot = snapshot.getBalle();

		Coord currentPosition = robot.getPosition();

		if (isFinished(snapshot)) {
			stop(controller);
			return;
		} else {
			// Fail quickly if not possible
			if (!isPossible(snapshot))
				return;
			if (turningExecutor.isFinished(snapshot)) {
				turningExecutor.stop(controller);
			}

			if (turningExecutor.isTurning()) // If we are still turning here
			{
				turningExecutor.step(controller, snapshot);
				return; // Continue
			} else {
				Orientation orientationToTarget = targetCoord.sub(
						currentPosition).orientation();
				turningExecutor.setTargetOrientation(orientationToTarget);
				double turnAngle = turningExecutor.getAngleToTurn(snapshot);
				double dist = targetCoord.dist(robot.getPosition());
				double distDiffFromTarget = Math
						.abs(Math.sin(turnAngle) * dist);

				// sin(180) = sin(0) thus the check
				if ((Math.abs(turnAngle) > Math.PI / 2)
						|| (Math.abs(distDiffFromTarget) > DISTANCE_DIFF_TO_TURN_FOR
								* dist)) {

					if (isMoving) {
						controller.stop();
						isMoving = false;
					}

					turningExecutor.step(controller, snapshot);
				} else {
                    controller.forward(movementSpeed);
                    isMoving = true;
				}
			}
		}

	}

	public void setMovementSpeed(int movementSpeed) {
		this.movementSpeed = movementSpeed;
	}

	@Override
	public void stop(Controller controller) {
		// If its doing anything, it will stop
		if (isMoving)
			controller.stop();

		// Otherwise it will just make sure to clean up
		isMoving = false;

		// Also make sure for turningExecutor to do the same
		if (turningExecutor != null) {
			turningExecutor.stop(controller);
		}

		// Note that we do not want to just call controller.stop()
		// blindly in case there are some other executors using it. (even though
		// there shouldn't be)
	}

	@Override
	public ArrayList<Drawable> getDrawables() {
		return new ArrayList<Drawable>();
	}

	@Override
	public void setStopDistance(double stopDistance) {
		this.stopDistance = stopDistance;

	}
}
