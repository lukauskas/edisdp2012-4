package balle.strategy.executor.movement;

import java.awt.Color;
import java.util.ArrayList;

import balle.controller.Controller;
import balle.main.drawable.Dot;
import balle.main.drawable.Drawable;
import balle.misc.Globals;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Snapshot;
import balle.world.objects.FieldObject;
import balle.world.objects.Robot;

public class GoToObjectPurePF implements MovementExecutor {

    private FieldObject         target;

	private double stopDistance = 0;
	private static final double ZERO_TH = Math.PI * 0.01;

	@Override
	public void stop(Controller controller) {
		controller.stop();
	}

	@Override
	public ArrayList<Drawable> getDrawables() {
		ArrayList<Drawable> l = new ArrayList<Drawable>();
		l.add(new Dot(1, 0, Color.gray));
		l.add(new Dot(1, 1, Color.gray));
		return l;
	}

	@Override
    public void updateTarget(FieldObject target) {
		this.target = target;

	}

	@Override
	public boolean isFinished(Snapshot snapshot) {
		if (true)
			return false;
		Robot robot = snapshot.getBalle();
		Coord currentPosition = robot.getPosition();
		if ((target == null) || (currentPosition == null)) {
			return false;
		}
		return currentPosition.dist(target.getPosition()) < stopDistance;
	}

	@Override
	public boolean isPossible(Snapshot snapshot) {
		Robot robot = snapshot.getBalle();
		Coord currentPosition = robot.getPosition();
		Orientation currentOrientation = robot.getOrientation();
		Coord targetPosition = (target != null) ? target.getPosition() : null;
		return ((currentOrientation != null) && (currentPosition != null) && (targetPosition != null));

	}

	@Override
	public void step(Controller controller, Snapshot snapshot) {
		Robot r = snapshot.getBalle();
		Orientation rO = r.getOrientation();
		Coord rP = r.getPosition();
		double rFO = rP.angleBetween(rP.add(rO.getUnitCoord()),
				target.getPosition()).atan2styleradians();
		int pR = (int) Math.round(getRightWheelSpeed(rFO));
		int pL = (int) Math.round(getLeftWheelSpeed(rFO));

		double dif = Math.abs(pR - pL);
		// slow down rotations
		double min = 0.3;
		double exp = 0.1;
		double rotSlowdown = (1 - ((double) Math.pow(dif, exp) / Math
.pow(
				(2 * Globals.MAXIMUM_MOTOR_SPEED), exp)));
		rotSlowdown = (rotSlowdown * (1 - min)) + min;
		pR *= rotSlowdown;
		pL *= rotSlowdown;
		// slowdown forward movement if near target and not facing it;
		double dist = r.getPosition().dist(target.getPosition());
		// double odSlowdown = (1/)

		// boolean left = Math.abs(pR) > Math.abs(pL);
		// if (left) {
		// pR = (pR + pL) / 2;
		// } else {
		// pL = (pR + pL) / 2;
		// }
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

	private static double getWheelSpeedHelper(double a) {
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
