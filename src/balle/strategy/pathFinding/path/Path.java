package balle.strategy.pathFinding.path;

import balle.strategy.curve.Curve;
import balle.world.objects.Robot;

public interface Path {

	/**
	 * get the curve that follows this path
	 * 
	 * @return
	 */
	public Curve getCurve();

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
	public int[] getPowers(Robot robot, double t);

	/**
	 * get The wheel velocities at any time t given the current wheel speeds
	 * 
	 * @rteturn [left, right] velocities
	 */
	public abstract double[] getVelocities(double t, Curve c,
			double leftWheelVel, double rightWheelVel);

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
	public double getTimeToDrive(Robot r);

	/**
	 * convert from real time to curve time such the pos( realTimeToCurveTime(1)
	 * ) is the position of the robot after 1 second
	 */
	public double realTimeToCurveTime(double time, Robot r);
}
