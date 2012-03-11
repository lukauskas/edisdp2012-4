package balle.strategy.bezierNav;

import balle.controller.Controller;
import balle.strategy.pathfinding.PathFinder;
import balle.world.Snapshot;
import balle.world.objects.Robot;

public class PidBezierNav extends BezierNav {

	public PidBezierNav(PathFinder pathfinder) {
		super(pathfinder);
	}

	@Override
	protected void setWheelSpeeds(Controller controller, Snapshot snapshot,
			double left, double right, long dT) {

		Robot robot = snapshot.getBalle();

		// if (dT > 0) {
		// if (lastAngle != null) {
		// // this uses the angular and linear velocity of the robot to
		// // find the estimated powers to the wheels
		// double dA = lastAngle.angleToatan2Radians(robot
		// .getOrientation());
		// double basicV = (dA * Globals.ROBOT_TRACK_WIDTH / 2) / dT;
		// int flipper = robot.getVelocity().dot(
		// robot.getOrientation().getUnitCoord()) <= 0 ? -1 : 1;
		// double curentLeftP = Globals
		// .velocityToPower((float) ((flipper * robot
		// .getVelocity().abs()) - basicV));
		// double curentRightP = Globals
		// .velocityToPower((float) ((flipper * robot
		// .getVelocity().abs()) + basicV));
		// // use PID
		// left = pid.convert(left, curentLeftP);
		// right = pid.convert(right, curentRightP);
		// }
		// lastAngleTime = snapshot.getTimestamp();
		// lastAngle = robot.getOrientation();
		// }

		System.out.println(left + "\t\t" + right);

		controller.setWheelSpeeds((int) left, (int) right);
	}

}
