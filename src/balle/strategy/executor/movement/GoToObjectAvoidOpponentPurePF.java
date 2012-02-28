package balle.strategy.executor.movement;

import java.util.ArrayList;

import balle.controller.Controller;
import balle.main.drawable.Drawable;
import balle.misc.Globals;
import balle.strategy.purePF.PFObject;
import balle.strategy.purePF.PullPointPurePFObject;
import balle.strategy.purePF.WallPurePFObject;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Snapshot;
import balle.world.objects.Robot;
import balle.world.objects.StaticFieldObject;

public class GoToObjectAvoidOpponentPurePF implements MovementExecutor {

	private StaticFieldObject target;

	private Snapshot currentState;

	private double stopDistance = 0;
	private final double ZERO_TH = Math.PI / 2;

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
		Robot ourRobot = currentState.getBalle();
		Coord rF, totalRelativeForce = new Coord(0, 0);

		// PFObject opponentPFO = new PFObject() {
		//
		// @Override
		// protected Coord relativePosToForce(Coord pos) {
		// if (pos.abs() > 0.5) {
		// return new Coord(0, 0);
		// }
		// return pos;
		// }
		// };

		PFObject objects[] = new PFObject[] {
				// ball
				new PullPointPurePFObject(currentState.getBall().getPosition(),
						1, 1),
				// opponent
				new PullPointPurePFObject(currentState.getOpponent()
						.getPosition(), -0.2, 2),
				// walls
				new WallPurePFObject(new Coord(0, Globals.PITCH_HEIGHT),
						new Orientation(-Math.PI / 2, true), 0.1, 2),
				new WallPurePFObject(new Coord(0, 0), new Orientation(
						Math.PI / 2, true), 0.1, 2) };

		for (PFObject o : objects) {
			rF = o.getRelativeForce(ourRobot.getPosition(),
					ourRobot.getOrientation());
			totalRelativeForce = totalRelativeForce.add(rF);
		}

		double rFO = totalRelativeForce.getOrientation().atan2styleradians();
		System.out.println(totalRelativeForce.abs());
		int pR = (int) Math.round(getRightWheelSpeed(rFO,
				totalRelativeForce.abs()));
		int pL = (int) Math.round(getLeftWheelSpeed(rFO,
				totalRelativeForce.abs()));
		controller.setWheelSpeeds(pL, pR);

	}

	/**
	 * @param a
	 *            atan2 style angle relative to current angle of force
	 * @return
	 */
	public double getLeftWheelSpeed(double a, double mag) {
		return getWheelSpeedHelper(a, mag);
	}

	/**
	 * @param a
	 *            atan2 style angle relative to current angle of force
	 * @return
	 */
	public double getRightWheelSpeed(double a, double mag) {
		return getWheelSpeedHelper(-a, mag);
	}

	private double getWheelSpeedHelper(double a, double mag) {
		double max = Globals.MAXIMUM_MOTOR_SPEED * mag, out;

		if (a < 0) {
			out = max;
		} else if (a < ZERO_TH) {
			out = max * (ZERO_TH - a) / ZERO_TH;
		} else {
			out = -max * (a - ZERO_TH) / (Math.PI - ZERO_TH);
		}

		return Math.min(out, Globals.MAXIMUM_MOTOR_SPEED);
	}

	@Override
	public void setStopDistance(double stopDistance) {
		this.stopDistance = stopDistance;
	}

}
