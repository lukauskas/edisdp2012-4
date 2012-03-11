package balle.strategy.bezierNav;

import balle.controller.Controller;
import balle.misc.Globals;
import balle.strategy.pathfinding.PathFinder;
import balle.world.Orientation;
import balle.world.Snapshot;
import balle.world.objects.Robot;

public class PidBezierNav extends BezierNav {

	// PID constants for the wheels.
	public static final double default_P = 0.25, default_I = 4, default_D = 1;

	// PID objects
	protected PID pidl = new PID(default_P, default_I, default_D),
			pidr = new PID(default_P, default_I, default_D);

	public PidBezierNav(PathFinder pathfinder) {
		super(pathfinder);
	}

	protected long lastAngleTime = 0;
	protected Orientation lastAngle = null;

	@Override
	protected void setWheelSpeeds(Controller controller, Snapshot snapshot,
			double left, double right, long dT) {

		Robot robot = snapshot.getBalle();

		if (dT > 0) {
			if (lastAngle != null) {
				// this uses the angular and linear velocity of the robot to
				// find the estimated powers to the wheels
				double dA = lastAngle.angleToatan2Radians(robot
						.getOrientation());
				double basicV = (dA * Globals.ROBOT_TRACK_WIDTH / 2) / dT;
				int flipper = robot.getVelocity().dot(
						robot.getOrientation().getUnitCoord()) <= 0 ? -1 : 1;
				double curentLeftP = Globals
						.velocityToPower((float) ((flipper * robot
								.getVelocity().abs()) - basicV));
				double curentRightP = Globals
						.velocityToPower((float) ((flipper * robot
								.getVelocity().abs()) + basicV));
				// use PID
				left = pidl.convert(left, curentLeftP);
				right = pidr.convert(right, curentRightP);
			}
			lastAngleTime = snapshot.getTimestamp();
			lastAngle = robot.getOrientation();
		}

		System.out.println(left + "\t\t" + right);

		controller.setWheelSpeeds((int) left, (int) right);
	}

}
