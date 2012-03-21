package balle.strategy.curve.path;

import balle.misc.Globals;
import balle.strategy.curve.Curve;
import balle.world.Coord;

public class MaxSpeedPath extends AbstractPath {

	private final double MIN_SAFE_RADIUS = 0; // the smallest turning radius
												// where moving at maximum speed
												// is ok (0.05)
	private final double SAFER_SPEED_RATIO = 0.5; // ratio of (max
													// speed)/((minimum)safe
													// speed). when making sharp
													// turns the speed will be
													// slowed toward max/this
	private final double MAX_VELOCITY = Globals.powerToVelocity(600); // the
																		// maximum
																		// wheel
																		// velocity
																		// to
																		// use
	private final double DAMPENING_POWERCHANGE = 0;
	private final double DAMPENING_POWERRATIO = 0; // increase towards 1 to make
													// the robot move more
													// strait

	public MaxSpeedPath(Curve curve) {
		super(curve);
	}

	@Override
	public double[] getVelocities(double t, Curve c, double leftWheelVel,
			double rightWheelVel) {
		// calculate turning radius
		Coord a = c.acc(0);
		double r = c.rad(0);

		boolean isLeft = new Coord(0, 0).angleBetween(
				c.vel(t).getOrientation().getUnitCoord(), a)
				.atan2styleradians() > 0;
		// throttle speed (slow when doing sharp turns)
		double max = MAX_VELOCITY;
		// // maximum speed is ok
		if (r < MIN_SAFE_RADIUS) {
			double min = max * SAFER_SPEED_RATIO;
			max = min + ((r / MIN_SAFE_RADIUS) * (max - min));
		}

		double v1, v2, left, right;
		v1 = (float) (max * getMinVelocityRato(r));
		v2 = (float) max;
		left = isLeft ? v1 : v2;
		right = isLeft ? v2 : v1;
		
		// dampen
		double invDampening = 1 - DAMPENING_POWERCHANGE;

		left = ((invDampening * left) + (DAMPENING_POWERCHANGE * Globals
				.velocityToPower((float) leftWheelVel)));
		right = ((invDampening * right) + (DAMPENING_POWERCHANGE * Globals
				.velocityToPower((float) rightWheelVel)));

		return new double[] { left, right };
	}

	private double getMinVelocityRato(double radius) {
		double rtw = Globals.ROBOT_TRACK_WIDTH / 2;
		double ratio = ((radius - rtw) / (radius + rtw));
		return ratio + ((1 - ratio) * DAMPENING_POWERRATIO);
	}

}
