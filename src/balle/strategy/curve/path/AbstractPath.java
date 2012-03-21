package balle.strategy.curve.path;

import balle.misc.Globals;
import balle.strategy.curve.Curve;
import balle.world.objects.Robot;

/**
 * A Path is like a curve, but also contains information about the speed of each
 * wheel at any point on the line Note that the robot will always be at the
 * start of the Path, and so the actually movement of the robot will be based on
 * the start of the path only. The rest is just for planning purposes.
 * 
 * A consequence of this is that in the case of a path that constantly increases
 * speed from 10 to 300 from start to end of the path will actually just result
 * in a constant speed of 10 because the robot is always at the start. In stead,
 * the path should look at the current wheel speeds and insure that the speeds
 * are set accordingly. In this case if the wheel speeds are 200, we may set the
 * wheel speed at t=0 to be 210.
 * 
 * @author s0906575
 * 
 */
public abstract class AbstractPath {

	private Curve curve;

	public AbstractPath(Curve curve) {
		this.curve = curve;
	}

	public Curve getCurve() {
		return curve;
	}

	/**
	 * Get the left and right powers to be sent to the motors
	 * 
	 * @param robot
	 *            curent state of the robot
	 * @param t
	 *            at what point on the line are we calculating powers for (0 is
	 *            the start)
	 * @return [left, right] powers
	 */
	public int[] getPowers(Robot robot, double t) {
		// this uses the angular and linear velocity of the robot to
		// find the estimated powers to the wheels
		// basicV is the velocity of each wheel assuming the robot is just
		// spinning
		double curentLeftV;
		double curentRightV;
		if (robot.getVelocity() != null && robot.getAngularVelocity() != null) {
			double basicV = robot.getAngularVelocity().radians()
					* Globals.ROBOT_TRACK_WIDTH / 2;
			int flipper = robot.getVelocity().dot(
					robot.getOrientation().getUnitCoord()) <= 0 ? -1 : 1;
			curentLeftV = (flipper * robot
							.getVelocity().abs()) - basicV;
			curentRightV = (flipper * robot
							.getVelocity().abs()) + basicV;
			
		} else {
			curentLeftV = curentRightV = 0;
		}
		double[] vels = getVelocities(t, curve, curentLeftV, curentRightV);
		return new int[] { (int) Globals.velocityToPower((float) vels[0]),
				(int) Globals.velocityToPower((float) vels[1]) };
	}

	/**
	 * get The wheel velocities at any time t given the current wheel speeds
	 * 
	 * @rteturn [left, right] velocities
	 */
	public abstract double[] getVelocities(double t, Curve c,
			double leftWheelVel,
			double rightWheelVel);


}
