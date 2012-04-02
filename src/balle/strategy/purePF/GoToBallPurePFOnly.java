package balle.strategy.purePF;

import balle.controller.Controller;
import balle.misc.Globals;
import balle.strategy.ConfusedException;
import balle.strategy.planner.AbstractPlanner;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Snapshot;
import balle.world.objects.Ball;

public class GoToBallPurePFOnly extends AbstractPlanner {

	private final double ZERO_TH = Math.PI / 5;

	private GoalDirectedPointPFObject ball;

	public GoToBallPurePFOnly() {
		ball = new GoalDirectedPointPFObject();
	}

	@Override
	public void onStep(Controller controller, Snapshot snapshot) throws ConfusedException {

		Ball b = snapshot.getBall();
		ball.update(b.getPosition(), snapshot.getOpponentsGoal());

		// Coord force = ball.getForce(s.getBalle().getPosition());
		// System.out.println(force);
		// find relative force
		// Coord rF = force.rotate(s.getBalle().getOrientation().getOpposite());
		// System.out.println(rF);
		// double rFO = rF.getOrientation().atan2styleradians();
		Coord rToB = snapshot.getBall().getPosition().sub(snapshot.getBalle().getPosition());
		Orientation rO = snapshot.getBalle().getOrientation();
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
