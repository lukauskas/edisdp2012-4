package balle.strategy.purePF;

import balle.controller.Controller;
import balle.misc.Globals;
import balle.strategy.planner.AbstractPlanner;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Snapshot;
import balle.world.objects.Ball;

public class GoToBallPFOnly extends AbstractPlanner {

	private final double ZERO_TH = Math.PI / 5;

	private GoalDirectedPointPFObject ball;

	public GoToBallPFOnly() {
		ball = new GoalDirectedPointPFObject();
	}

	@Override
	public void step(Controller controller) {
		Snapshot s = getSnapshot();

		Ball b = s.getBall();
		ball.update(b.getPosition(), s.getOpponentsGoal());

		// Coord force = ball.getForce(s.getBalle().getPosition());
		// System.out.println(force);
		// find relative force
		// Coord rF = force.rotate(s.getBalle().getOrientation().getOpposite());
		// System.out.println(rF);
		// double rFO = rF.getOrientation().atan2styleradians();
		Coord rToB = s.getBall().getPosition().sub(s.getBalle().getPosition());
		Orientation rO = s.getBalle().getOrientation();
		double rFO = rToB.rotate(rO.getOpposite()).getOrientation()
				.atan2styleradians();
		System.out.println(rFO);
		int pR = (int) Math.round(getRightWheelSpeed(rFO));
		int pL = (int) Math.round(getLeftWheelSpeed(rFO));
		System.out.println(pL + "\t\t" + pR);
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
}
