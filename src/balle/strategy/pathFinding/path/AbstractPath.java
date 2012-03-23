package balle.strategy.pathFinding.path;

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
public abstract class AbstractPath implements Path {

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
		double[] currVels = Globals.getWheelVels(robot.getVelocity(),
				robot.getAngularVelocity(), robot.getOrientation());
		double currentLeftV = currVels[0];
		double currentRightV = currVels[1];
		double[] vels = getVelocities(t, curve, currentLeftV, currentRightV);
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

	/**
	 * get an approximation of the time it will take for the robot to drive this
	 * path.
	 * 
	 * note that this assumes the path is not stupid and actually gives powers
	 * that move along the curve
	 * 
	 * @param lv
	 *            initial left wheel velocity
	 * @param lr
	 *            initial right wheel velocity
	 * @return
	 */
	public double getTimeToDrive(Robot r) {
		double lv, rv;
		double[] vis = Globals.getWheelVels(r);
		lv = vis[0];
		rv = vis[1];
		Curve c = getCurve();
		double t = 0;
		final double STEP = 1.0 / 20.0;
		for (double i = 0; i < 1; i += STEP) {
			// find the time for this step t = d/v
			double dist = c.pos(i).dist(c.pos(i + STEP));
			double[] vels = getVelocities(i, c, lv, rv);
			double vel = Math.abs((vels[0] + vels[1]) / 2);
			t += dist / vel;
			lv = vels[0]; // set the left and right velocities of the wheels
			rv = vels[1]; // used for the next step
		}
		return t;
	}
	
	/**
	 * convert from real time to curve time such the pos( realTimeToCurveTime(1) ) is the position of the robot after 1 second
	 */
	public double realTimeToCurveTime(double time, Robot r) {
		double lv, rv;
		double[] vis = Globals.getWheelVels(r);
		lv = vis[0];
		rv = vis[1];
		Curve c = getCurve();
		double t = 0;
		final double STEP = 1.0 / 20.0;
		for (double i = 0; i < 1; i += STEP) {
			// find the time for this step t = d/v
			double dist = c.pos(i).dist(c.pos(i + STEP));
			double[] vels = getVelocities(i, c, lv, rv);
			double vel = Math.abs((vels[0] + vels[1]) / 2);
			t += dist / vel;
			lv = vels[0]; // set the left and right velocities of the wheels
			rv = vels[1]; // used for the next step
			if (t >= time)
				return i;
		}
		return 1;
	}

}
