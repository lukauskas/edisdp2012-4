package balle.strategy.bezierNav;

import static balle.misc.Globals.MAXIMUM_MOTOR_SPEED;
import static balle.misc.Globals.velocityToPower;
import balle.controller.Controller;
import balle.misc.Globals;
import balle.strategy.pathFinding.PathFinder;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Snapshot;
import balle.world.objects.Robot;

public class PidBezierNav extends BezierNav {

	// PID constants for the wheels.
	public static final int default_history = 3;
	public static final double default_P = 0.5, default_I = 0.5,
			default_D = 0.0;

	// PID objects
	protected PID pidl = new PID(default_history, default_P, default_I,
			default_D), pidr = new PID(default_history, default_P, default_I,
			default_D);

	public PidBezierNav(PathFinder pathfinder) {
		super(pathfinder);
	}

	protected long lastAngleTime = 0;
	protected Orientation lastAngle = null;
	protected Coord lastPos = null;

	protected void setWheelSpeeds(Controller controller, Snapshot snapshot,
			double left, double right, long dT) {

		Robot robot = snapshot.getBalle();

		if (dT > 0) {
			if (lastPos != null) {

				// Calculate base velocity
				Coord diff = robot.getPosition().sub(lastPos);
				double vel = diff.abs() * (1000 / (dT + 1));

				// this uses the angular and linear velocity of the robot to
				// find the estimated powers to the wheels
				double dA = lastAngle.angleToatan2Radians(robot
						.getOrientation());

				double basicV = (dA * Globals.ROBOT_TRACK_WIDTH / 2) / dT;

				int flipper = robot.getVelocity().dot(
						robot.getOrientation().getUnitCoord()) <= 0 ? -1 : 1;

				double curentLeftP = vel
						+ velocityToPower((float) ((flipper * robot
								.getVelocity().abs()) - basicV));
				double curentRightP = vel
						+ velocityToPower((float) ((flipper * robot
								.getVelocity().abs()) + basicV));

				curentLeftP /= 0.5;
				curentRightP /= 0.5;

				System.out.println(">>>>>>>>>>>>>\n" + left + "\t\t" + right
						+ "\n" + curentLeftP + "\t\t" + curentRightP);

				// use PID
				left = pidl.convert(left, curentLeftP);
				right = pidr.convert(right, curentRightP);

				System.out.println(left + "\t\t" + right);

			}

			lastAngleTime = snapshot.getTimestamp();
			lastAngle = robot.getOrientation();
			lastPos = robot.getPosition();

			int powl = (int) (left * MAXIMUM_MOTOR_SPEED), powr = (int) (right * MAXIMUM_MOTOR_SPEED);
			System.out.println(powl + "\t\t" + powr);
			controller.setWheelSpeeds(powl, powr);
		}

	}

}
