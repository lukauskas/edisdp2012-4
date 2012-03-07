package balle.strategy.bezierNav;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import balle.controller.Controller;
import balle.main.drawable.Circle;
import balle.main.drawable.Dot;
import balle.main.drawable.Drawable;
import balle.misc.Globals;
import balle.strategy.curve.Curve;
import balle.strategy.executor.movement.OrientedMovementExecutor;
import balle.strategy.pathfinding.PathFinder;
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
			.powerToVelocity(Globals.MAXIMUM_MOTOR_SPEED); // the maximum
																// wheel
															// velocity to use

	private static final double SUBTARGET_RADIUS = 0.05; // how close the robot
															// has
														// to be to intermitent
														// points befor it try
														// to get to the next
														// point

	// these 2 must moth be satisfied before this movement is finished
	private double stopDistance = 0.03; // distance to target (centre of robot
										// to adjusted p3)
	private double stopAngle = Math.PI / 13; // angle of robot vs desired final
												// angle (orient)
	private PID pid = new PID(0.3, 0.7, 0.5);
	private Orientation lastAngle;
	private long lastAngleTime;



	private ArrayList<Coord> targetPoints = new ArrayList<Coord>(
			Arrays.asList(new Coord[] {
 new Coord(0.5, 0.3), new Coord(1, 1),
					new Coord(0.2, 0.4), new Coord(0.5, 0.3),

			}));
	
	private PathFinder pathfinder;
	private Curve c;

	private StaticFieldObject target;

	private Orientation orient;
	private Coord p0, p3;

	// James, sorry, it was just easier this way.
	private double p = 1, i = 1, d = 1;
	private PID left = new PID(p, d, i), right = new PID(p, d, i);

	public BezierNav(PathFinder pathfinder) {
		this.pathfinder = pathfinder;
	}

	private boolean isMoveStraitFinished(Orientation botOrient) {
		Coord n = botOrient.getUnitCoord()
				.rotate(new Orientation(Math.PI / 2));
		double da = Math
				.abs(botOrient.angleToatan2Radians(orient));
		double dd = Math.abs(n.dot(target.getPosition().sub(p0)));
		return (da <= stopAngle && dd < (Globals.ROBOT_WIDTH / 2)
				- TARGET_OFF_CENTER_TOLERANCE);
	}

	@Override
	public boolean isFinished(Snapshot snapshot) {
		if (p0 == null || p3 == null) {
			// haven't even started
			return false;
		}
		return snapshot.getBalle().getPosition().dist(getAdjustedP3()) <= stopDistance
				&& isMoveStraitFinished(snapshot.getBalle().getOrientation());
	}

	@Override
	public boolean isPossible(Snapshot snapshot) {
		return true;
	}

	@Override
	public void step(Controller controller, Snapshot snapshot) {
		// controller.kick();
		if (isFinished(snapshot)) {
			stop(controller);
			return;
		}
		// calculate bezier points 0 to 3
		Robot robot = snapshot.getBalle();
		Coord rP = robot.getPosition(), tP = getAdjustedP3();
		if (rP == null || tP == null) {
			return;
		}

		p0 = rP;
		p3 = tP;

		// if we are close to the target and facing the correct orientation
		// (orient)
		// just go strait to ball
		Coord n = robot.getOrientation().getUnitCoord()
				.rotate(new Orientation(Math.PI / 2));
		double da = Math
				.abs(robot.getOrientation()
				.angleToatan2Radians(orient));
		double dd = Math.abs(n.dot(target.getPosition().sub(p0)));
		if ((da <= Math.PI / 2 && dd < (Globals.ROBOT_WIDTH / 2)
				- TARGET_OFF_CENTER_TOLERANCE)) {
			p3 = target.getPosition();
		}

		// remove current target point if close
		if (targetPoints.size() > 0
				&& p0.dist(targetPoints.get(0)) <= SUBTARGET_RADIUS) {
			targetPoints.remove(0);
		}

		Coord[] tpa = new Coord[targetPoints.size() + 2];
		tpa[0] = p0;
		tpa[tpa.length - 1] = p3;
		for (int i = 0; i < targetPoints.size(); i++) {
			tpa[i + 1] = targetPoints.get(i);
		}

		c = pathfinder.getPath(snapshot, robot.getPosition(),
				robot.getOrientation(), tP, orient);
		
		// calculate turning radius
		Coord a = c.acc(0);
		boolean isLeft = new Coord(0, 0).angleBetween(
				robot.getOrientation().getUnitCoord(), a)
				.atan2styleradians() > 0;
		double r = c.rad(0);

		// throttle speed (slow when doing sharp turns)
		double max = MAX_VELOCITY;
		// // maximum speed is ok
		if (r < MIN_SAFE_RADIUS) {
			double min = max * SAFER_SPEED_RATIO;
			max = min + ((r / MIN_SAFE_RADIUS) * (max - min));
		}

		// calculate wheel speeds/powers
		double v1, v2, left, right;
		v1 = Globals
				.velocityToPower((float) (max * getMinVelocityRato(r)));
		v2 = Globals.velocityToPower((float) max);
		left = isLeft ? v1 : v2;
		right = isLeft ? v2 : v1;
		// find current wheel powers
		long dT = snapshot.getTimestamp() - lastAngleTime;
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
				left = pid.convert(left, curentLeftP);
				right = pid.convert(right, curentRightP);
			}
			lastAngleTime = snapshot.getTimestamp();
			lastAngle = robot.getOrientation();
		}
		controller.setWheelSpeeds((int) left, (int) right);

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

		l.addAll(pathfinder.getDrawables());

		if (c != null) {
			l.add(c);
			Coord center = c.cor(0);
			l.add(new Dot(center, Color.BLACK));
			// l.add(new Circle(center, center
			// .dist(snapshot.getBalle().getPosition()),
			// Color.yellow));
		}

		l.add(new Circle(p0, 0.03, Color.pink));
		l.add(new Circle(p3, 0.03, Color.pink));

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
