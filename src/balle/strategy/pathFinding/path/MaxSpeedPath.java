package balle.strategy.pathFinding.path;

import org.apache.log4j.Logger;

import balle.misc.Globals;
import balle.strategy.curve.Curve;
import balle.world.Coord;

public class MaxSpeedPath extends AbstractPath {
	public static final Logger LOG = Logger.getLogger(MaxSpeedPath.class);

	private final double MIN_SAFE_RADIUS = 0.75; // the smallest turning radius
												// where moving at maximum speed
												// is ok (0.05)
	private final double SAFER_SPEED_RATIO = 0.9; // ratio of (max
													// speed)/((minimum)safe
													// speed). when making sharp
													// turns the speed will be
													// slowed toward max/this
	private final double APPROACH_SPEEDRATIO = 0.2; // when near the target
														// slow down to this
														// ratio of max speed
	private final double APPROACH_DISTANCE = 2; // Approaching if within this
													// distance
	private final double MAX_VELOCITY = 1.0; // Globals.powerToVelocity(900); //
												// the
																		// maximum
																		// wheel
																		// velocity
																		// to
																		// use

	private final double DAMPENING_POWERCHANGE = 0;
	private final double DAMPENING_POWERRATIO = 0; // increase towards 1 to
														// make
													// the robot move more
													// strait

	public MaxSpeedPath(Curve curve) {
		super(curve);
	}

	@Override
	public double[] getVelocities(double t, Curve c, double leftWheelVel,
			double rightWheelVel) {
		
		LOG.trace(MAX_VELOCITY);
		
		// calculate turning radius
		Coord a = c.acc(0);
		double r = c.rad(0);

		boolean isLeft = new Coord(0, 0).angleBetween(
				c.vel(t).getOrientation().getUnitCoord(), a)
				.atan2styleradians() > 0;
		// throttle speed (slow when doing sharp turns or approaching)
		double max = MAX_VELOCITY;
		double distToTarget = c.pos(t).dist(c.pos(1));
        // System.out.println("dist to target: " + distToTarget);
		if (distToTarget < APPROACH_DISTANCE) {
			double closeness = 1 - (distToTarget / APPROACH_DISTANCE);
			max = (closeness * (max * APPROACH_SPEEDRATIO))
					+ ((1 - closeness) * max);
		}
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
