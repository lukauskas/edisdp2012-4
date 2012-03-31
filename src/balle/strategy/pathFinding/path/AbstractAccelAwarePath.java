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
 * This abstract path assumes that the robot has instant acceleration.
 * 
 * @author s0906575
 * 
 */
public abstract class AbstractAccelAwarePath implements Path {

	public static double A = 20; // higher A means the robot can
										// accelerate
									// faster
	public static double SCALE_TERM_VELS = 1.2;
	public static double HALF_LIFE = 50;

	private Curve curve;

	public AbstractAccelAwarePath(Curve curve) {
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
		double currentLeftV = currVels[0] * 1000;
		double currentRightV = currVels[1] * 1000;
		double[] velsF = getVelocities(t, curve, currentLeftV, currentRightV);

		// !!!!!!!!!!!
		// this is incorrect when t != 0 (because realTimeToCurveTime asumes we
		// start at t=0)
		// double dCT = 0.001;
		// // double dCT = (Math.min(0.9999999,
		// // t + this.realTimeToCurveTime(dT, robot)) - t);
		// double linV = (velsI[0] + velsI[1]) / 2;
		// double dist = getCurve().pos(t).dist(getCurve().pos(t + dCT));
		// double dT = dist / linV;
		double dT = 50;
		
		System.out.println("dt = " + dT);
		// double[] velsF = getVelocities(t + dCT, curve,
		// currentLeftV, currentRightV);
		// !!!!!!!!!!!

		// use acceleration not velocities
		double[] accels = new double[] { (velsF[0] - currentLeftV) / dT,
				(velsF[1] - currentRightV) / dT };

		int[] newPs = accelsToPowers(accels[0], accels[1], currentLeftV,
				currentRightV);

		System.out.println("-----left------_");
		System.out.println(Globals.velocityToPower((float) velsF[0]));
		System.out.println(newPs[0]);
		System.out.println("---------------_");

		return newPs;
	}

	/**
	 * 
	 * Given the current wheel velocities and desired acceleration, this
	 * function will calculate what power the motors should have to achieve the
	 * desired accelerations.
	 * 
	 * @param al
	 *            left wheel accel
	 * @param ar
	 *            right wheel accel
	 * @param vl
	 *            initial left wheel velocity
	 * @param vr
	 *            initial right wheel velocity
	 * @return The power the motors should have to achieve the desired
	 *         accelerations.
	 */
	private int[] accelsToPowers(double al, double ar, double vl, double vr) {
		return new int[] { findBestPower(vl, al), findBestPower(vr, ar) };
	}

	/**
	 * 
	 * @param vi
	 *            current wheel velocity
	 * @param a
	 *            desired wheel acceleration
	 * @return motor power to set
	 */
	private int findBestPower(double vi, double a) {
		// this is not working atm
		if (1 == 2) {
			double closeness = Double.MAX_VALUE;
			int bestP = 0;
			for (int p = -900; p <= 900; p++) {
				double CurrCloseness = Math.abs(accelAtVelWithPow(p, vi) - a);
				if (closeness > CurrCloseness) {
					closeness = CurrCloseness;
					bestP = p;
				}
			}

			return bestP;
		}

		// use simplified version.... use a half life to terminal vel
		int bestP = 0;
		double bestAC = Double.MAX_VALUE;
		for (int p = -900; p <= 900; p++) {
			double vf = (powToTermVel(p) + vi);
			double currAC = Math.abs(((vf - vi) / HALF_LIFE) - a);
			if (currAC < bestAC) {
				bestP = p;
				bestAC = currAC;
			}
		}
		
		return bestP;
	}
	
	/**
	 * Finds the wheel acceleration given the motor power and current wheel velocity
	 * @param pow motor power
	 * @param v	wheel velocity
	 * @return wheel acceleration
	 */
	private double accelAtVelWithPow(int pow, double v) {
		double vMax = powToTermVel(pow);
		boolean neg = v < 0;
		if (neg) {
		}
		boolean invert = false;
		if(v > vMax) {
			invert = true;
			v -=  2 * (v-vMax);
		}
		double t = -Math.log((1/((v/vMax)+0.5)) - 0.5)/A;
		
		double tmp = Math.exp(-t*A);
		double den = 1 + tmp;
		den = den * den;
		double a = vMax * ((A * tmp) / den);
		return invert ? -a : a;
	}

	/**
	 * find the terminal wheel velocity at a motor power
	 */
	private double powToTermVel(int pow) {
		// assume vels from Globals are underestimated terminal vels
		return 1.2 * Globals.powerToVelocity(pow);
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
		final double STEP = 1.0 / 40.0;
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
