package balle.strategy.executor.movement;

import java.util.ArrayList;

import balle.controller.Controller;
import balle.main.drawable.Drawable;
import balle.misc.Globals;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Snapshot;
import balle.world.objects.Robot;
import balle.world.objects.StaticFieldObject;

public class GoToObjectPurePF implements MovementExecutor {

	private StaticFieldObject target;

	private Snapshot currentState;

	private double stopDistance = 0;
	private final double ZERO_TH = Math.PI / 5;

	@Override
	public void stop(Controller controller) {
		controller.stop();
	}

	@Override
	public ArrayList<Drawable> getDrawables() {
		return new ArrayList<Drawable>();
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
		return currentPosition.dist(target.getPosition()) < stopDistance;
	}

	@Override
	public boolean isPossible() {
		Robot robot = currentState.getBalle();
		Coord currentPosition = robot.getPosition();
		Orientation currentOrientation = robot.getOrientation();
		Coord targetPosition = (target != null) ? target.getPosition() : null;
		return ((currentOrientation != null) && (currentPosition != null) && (targetPosition != null));

	}

	@Override
	public void updateState(Snapshot snapshot) {
		this.currentState = snapshot;

	}

	@Override
	public void step(Controller controller) {
		Coord rToB = target.getPosition().sub(
				currentState.getBalle().getPosition());
		Orientation rO = currentState.getBalle().getOrientation();
		double rFO = rToB.rotate(rO.getOpposite()).getOrientation()
				.atan2styleradians();
		int pR = (int) Math.round(getRightWheelSpeed(rFO));
		int pL = (int) Math.round(getLeftWheelSpeed(rFO));
		controller.setWheelSpeeds(pL, pR);
	}

	/**
	 * @param a
	 *            atan2 style angle relative to current angle of force
	 * @return
	 */
	public double getLeftWheelSpeed(double a) {
		return getWheelSpeedHelper(a);
	}

	/**
	 * @param a
	 *            atan2 style angle relative to current angle of force
	 * @return
	 */
	public double getRightWheelSpeed(double a) {
		return getWheelSpeedHelper(-a);
	}

	private double getWheelSpeedHelper(double a) {
		int max = Globals.MAXIMUM_MOTOR_SPEED;
		if (a < 0) {
			return max;
		} else if (a < ZERO_TH) {
			return max * (ZERO_TH - a) / ZERO_TH;
		} else {
			return -max * (a - ZERO_TH) / (Math.PI - ZERO_TH);
		}
	}

	@Override
	public void setStopDistance(double stopDistance) {
		this.stopDistance = stopDistance;
	}

}
