package balle.strategy.executor.movement;

import java.util.ArrayList;

import balle.controller.Controller;
import balle.main.drawable.Drawable;
import balle.strategy.executor.turning.RotateToOrientationExecutor;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Snapshot;
import balle.world.objects.Robot;
import balle.world.objects.StaticFieldObject;

public class GoToObject implements MovementExecutor {

	private double stopDistance = 0.2;

	private final static double EPSILON = 0.00001;

	protected StaticFieldObject target = null;
	protected Snapshot currentState = null;
	private boolean isMoving = false;

	private final static double DISTANCE_DIFF_TO_TURN_FOR = 0.3;
	private final static int MOVEMENT_SPEED = 500;

	RotateToOrientationExecutor turningExecutor = null;

	public GoToObject(RotateToOrientationExecutor turningExecutor) {
		this.turningExecutor = turningExecutor;
	}

	@Override
	public void updateTarget(StaticFieldObject target) {
		this.target = target;
	}

	@Override
	public boolean isFinished() {
		Robot robot = currentState.getBalle();
		Coord currentPosition = robot.getPosition();
		if ((target == null) || (currentPosition == null)) {
			return false;
		}
		return ((currentPosition.dist(target.getPosition()) - stopDistance) < EPSILON);
	}

	@Override
	public boolean isPossible() {
		if (turningExecutor == null)
			return false;

		Robot robot = currentState.getBalle();
		Coord currentPosition = robot.getPosition();
		Orientation currentOrientation = robot.getOrientation();
		Coord targetPosition = (target != null) ? target.getPosition() : null;
		return ((currentOrientation != null) && (currentPosition != null) && (targetPosition != null));
	}

	@Override
	public void updateState(Snapshot snapshot) {
		currentState = snapshot;
	}

	@Override
	public void step(Controller controller) {
		// Fail quickly if state not set
		if (currentState == null)
			return;

		Coord targetCoord = target.getPosition();
		Robot robot = currentState.getBalle();

		Coord currentPosition = robot.getPosition();

		if (isFinished()) {
			stop(controller);
			return;
		} else {
			// Fail quickly if not possible
			if (!isPossible())
				return;
			turningExecutor.updateState(currentState);
			if (turningExecutor.isFinished()) {
				turningExecutor.stop(controller);
			}

			if (turningExecutor.isTurning()) // If we are still turning here
			{
				turningExecutor.step(controller);
				return; // Continue
			} else {
				Orientation orientationToTarget = targetCoord.sub(
						currentPosition).orientation();
				turningExecutor.setTargetOrientation(orientationToTarget);
				double turnAngle = turningExecutor.getAngleToTurn();
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

					turningExecutor.step(controller);
				} else {
					if (!isMoving) {
						controller.forward(MOVEMENT_SPEED);
						isMoving = true;
					}
				}
			}
		}

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
