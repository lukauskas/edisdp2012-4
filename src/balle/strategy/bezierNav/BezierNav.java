package balle.strategy.bezierNav;

import java.awt.Color;
import java.util.ArrayList;

import balle.controller.Controller;
import balle.main.drawable.Circle;
import balle.main.drawable.Dot;
import balle.main.drawable.Drawable;
import balle.misc.Globals;
import balle.strategy.curve.Curve;
import balle.strategy.curve.Interpolator;
import balle.strategy.curve.Spline;
import balle.strategy.executor.movement.OrientedMovementExecutor;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Snapshot;
import balle.world.objects.Robot;
import balle.world.objects.StaticFieldObject;

public class BezierNav implements OrientedMovementExecutor {

	// this is how far away from the target that the robot (center) should land
	// the default causes the robot to stop just in front of the target.
	// If this was 0 and the target was the ball, the robot would be prone
	// to crashing into the ball b4 obtainning the correct orientation.
	private final double TARGET_PERIMETER = (Math.sqrt(Math.pow(
			Globals.ROBOT_LENGTH, 2) + Math.pow(Globals.ROBOT_WIDTH, 2)) / 2) + 0.01;

	private final double TARGET_OFF_CENTER_TOLERANCE = 0.01; // (ROBOTWIDTH/2)-
														// TARGET_OFF_CENTER_TOLERANCE
														// = max arrival
														// distance of the
														// target from the
														// center front of the
														// robot
	private final double MIN_SAFE_RADIUS = 0.3; // the smallest turning radius
												// where moving at maximum speed
												// is ok
	private final double SAFER_SPEED_RATIO = 0.2; // ratio of (max
													// speed)/((minimum)safe
													// speed). when making sharp
													// turns the speed will be
													// slowed toward max/this
	private final double MAX_VELOCITY = Globals
			.powerToVelocity(Globals.MAXIMUM_MOTOR_SPEED); // the maximum wheel
															// velocity to use

	// these 2 must moth be satisfied before this movement is finished
	private double stopDistance = 0.03; // distance to target (centre of robot
										// to adjusted p3)
	private double stopAngle = Math.PI / 32; // angle of robot vs desired final
												// angle (orient)


	private Interpolator interpolator;
	private Curve c;

	private StaticFieldObject target;
	private Snapshot state;

	private Orientation orient;
	private Coord p0, p3;


	public BezierNav(Interpolator interpolator) {
		this.interpolator = interpolator;
	}

	@Override
	public boolean isFinished() {
		if (p0 == null || p3 == null) {
			// haven't even started
			return false;
		}
		return state.getBalle().getPosition().dist(getAdjustedP3()) <= stopDistance
				&& Math.abs(state.getBalle().getOrientation()
						.angleToatan2Radians(orient)) <= stopAngle;
		// return p0.add(
		// new Coord(0, Globals.ROBOT_LENGTH / 2).rotate(state.getBalle()
		// .getOrientation())).dist(target.getPosition()) <= stopDistance;
	}

	@Override
	public boolean isPossible() {
		return true;
	}

	@Override
	public void updateState(Snapshot snapshot) {
		state = snapshot;
	}

	@Override
	public void step(Controller controller) {
		// controller.kick();
		if (isFinished()) {
			stop(controller);
			return;
		}
		// calculate bezier points 0 to 3
		Robot robot = state.getBalle();
		Coord rP = robot.getPosition(), tP = getAdjustedP3();
		if (rP == null || tP == null) {
			return;
		}
		p0 = rP;
		p3 = tP;

		c = interpolator.getCurve(new Coord[] { p0, new Coord(0.5, 0.3), p3 },
				robot.getOrientation(), orient);

		// if we are close to the target and facing the correct orientation
		// (orient)
		// just go strait to ball
		Coord n = robot.getOrientation().getUnitCoord()
				.rotate(new Orientation(Math.PI / 2));
		double da = Math
				.abs(robot.getOrientation()
				.angleToatan2Radians(orient));
		double dd = Math.abs(n.dot(target.getPosition().sub(p0)));
		if ((da <= Math.PI / 2 && dd < (Globals.ROBOT_HEIGHT / 2)
				- TARGET_OFF_CENTER_TOLERANCE)) {
			p3 = target.getPosition();
		}

		
		
		// calculate turning radius
		Coord a = c.acc(0);
		boolean isLeft = new Coord(0, 0).angleBetween(
				robot.getOrientation().getUnitCoord(), a)
				.atan2styleradians() > 0;
		double r = c.rad(0);
		System.out.println(r);

		// throttle speed (slow when doing sharp turns)
		double max = MAX_VELOCITY;
		// // maximum speed is ok
		if (r < MIN_SAFE_RADIUS) {
			double min = max * SAFER_SPEED_RATIO;
			max = min + ((r / MIN_SAFE_RADIUS) * (max - min));
		}

		// calculate wheel speeds/powers
		int v1, v2;
		v1 = (int) Globals
				.velocityToPower((float) (max * getMinVelocityRato(r)));
		v2 = (int) Globals.velocityToPower((float) max);
		System.out.println(v2);
		controller.setWheelSpeeds(isLeft ? v1 : v2, isLeft ? v2 : v1);

		// apply wheel speeds using sum movement executer
		// if (false) {
		// movementExecutor.updateState(state);
		// movementExecutor.updateTarget(pos(stepDist).getPoint());
		// movementExecutor.step(controller);
		// }

	}

	@Override
	public ArrayList<Drawable> getDrawables() {
		ArrayList<Drawable> l = new ArrayList<Drawable>();
		if (p0 == null) {
			return l;
		}
		if (c instanceof Spline) {
			l.add((Spline) c);
		}
		l.add(new Circle(p0, 0.03, Color.pink));
		l.add(new Circle(p3, 0.03, Color.pink));
		Coord center = c.cor(0);
		l.add(new Dot(center, Color.BLACK));
		l.add(new Circle(center, center.dist(state.getBalle().getPosition()),
				Color.yellow));

		return l;
	}


	/**
	 * P3 (target) must be adjusted so that the front of the robot doesn't bump
	 * into the actual target
	 * @return
	 */
	private Coord getAdjustedP3() {
		return target.getPosition().add(
				new Coord(-TARGET_PERIMETER, 0).rotate(orient));
	}

	@Override
	public void setStopDistance(double stopDistance) {
		this.stopDistance = stopDistance;
	}

	private double getMinVelocityRato(double radius) {
		double rtw = Globals.ROBOT_TRACK_WIDTH / 2;
		return ((radius - rtw) / (radius + rtw));
	}

	@Override
	public void stop(Controller controller) {
		// controller.kick();
		controller.stop();
	}

	@Override
	public void updateTarget(StaticFieldObject target, Orientation o) {
		this.target = target;
		this.orient = o;
	}
}
