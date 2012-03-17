package balle.world.objects;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import balle.misc.Globals;
import balle.world.AngularVelocity;
import balle.world.Coord;
import balle.world.Line;
import balle.world.Orientation;
import balle.world.Velocity;

public class RobotTest {

	/**
	 * Given a robot and a ball that is located just in front of it, the robot
	 * should be marked as in possession of the ball
	 */
	@Test
	public void testPossessesBall() {
		Robot robot = new Robot(new Coord(0.5, 0.5), new Velocity(0, 0, 1),
				new AngularVelocity(0, 1),
				new Orientation(0, true));

		Ball ball = new Ball(new Coord(0.5 + robot.getHeight() / 2
				+ Globals.BALL_RADIUS, 0.5), new Velocity(0, 0, 1));

		assertTrue(robot.possessesBall(ball));

	}

	/**
	 * Given a robot and a ball that is located just in front of it, the robot
	 * should be marked as in possession of the ball. Different from
	 * testPosessesBall just in the orientation of the robot.
	 */
	@Test
	public void testPossessesBallOtherWayAround() {
		Robot robot = new Robot(new Coord(0.5, 0.5), new Velocity(0, 0, 1),
				new AngularVelocity(0, 1),
				new Orientation(-Math.PI, true));

		Ball ball = new Ball(new Coord(0.5 - robot.getHeight() / 2
				- Globals.BALL_RADIUS, 0.5), new Velocity(0, 0, 1));

		assertTrue(robot.possessesBall(ball));

	}

	/**
	 * Given a robot, robot's facing line should contain a point that is just in
	 * front of it.
	 */
	@Test
	public void testFacingLine() {

		Robot robot = new Robot(new Coord(0.5, 0.5), new Velocity(0, 0, 1),
				new AngularVelocity(0, 1),
				new Orientation(0, true));
		Line facingLine = robot.getFacingLine();
		Coord testPoint = new Coord(0.5 + robot.getHeight() / 2, 0.5);
		assertTrue(facingLine.contains(testPoint));

	}

	/**
	 * Given a robot, robot's facing line should contain a point that is just in
	 * front of it. Same to the testFacingLine, only difference is robot's
	 * orientation
	 */
	@Test
	public void testFacingLineOtherwayAround() {

		Robot robot = new Robot(new Coord(0.5, 0.5), new Velocity(0, 0, 1),
				new AngularVelocity(0, 1),
				new Orientation(Math.PI, true));
		Line facingLine = robot.getFacingLine();
		Coord testPoint = new Coord(0.5 - robot.getHeight() / 2, 0.5);
		assertTrue(facingLine.contains(testPoint));

	}

	/**
	 * Given a robot and a ball that is located just in front of it, the ball
	 * kick line should contain a point that is 1 ball radius away from the
	 * center of the ball towards the robot's orientation.
	 */
	@Test
	public void testBallKickLine() {

		Robot robot = new Robot(new Coord(0.5, 0.5), new Velocity(0, 0, 1),
				new AngularVelocity(0, 1),
				new Orientation(0, true));

		Ball ball = new Ball(new Coord(0.5 + robot.getHeight() / 2
				+ Globals.BALL_RADIUS, 0.5), new Velocity(0, 0, 1));

		Line ballKickLine = robot.getBallKickLine(ball);
		Coord testPoint = new Coord(0.5 + robot.getHeight() / 2 + 2
				* Globals.BALL_RADIUS, 0.5);
		assertTrue(ballKickLine.contains(testPoint));

	}

	/**
	 * Given a robot and a ball that is located just in front of it, the ball
	 * kick line should contain a point that is 1 ball radius away from the
	 * center of the ball towards the robot's orientation.
	 * 
	 * Same as testBallKickLine, but robot's orienation is other way around
	 */
	@Test
	public void testBallKickLineOtherWayAround() {

		Robot robot = new Robot(new Coord(0.5, 0.5), new Velocity(0, 0, 1),
				new AngularVelocity(0, 1),
				new Orientation(Math.PI, true));

		Ball ball = new Ball(new Coord(0.5 - robot.getHeight() / 2
				- Globals.BALL_RADIUS, 0.5), new Velocity(0, 0, 1));

		Line ballKickLine = robot.getBallKickLine(ball);
		Coord testPoint = new Coord(0.5 - robot.getHeight() / 2 - 2
				* Globals.BALL_RADIUS, 0.5);
		assertTrue(ballKickLine.contains(testPoint));

	}

	/**
	 * Given a robot, robot's facing line should contain a point that is just in
	 * front of it and just next to its back, and the robot itself.
	 */
	@Test
	public void testFacingLineBothWay() {

		Robot robot = new Robot(new Coord(0.5, 0.5), new Velocity(0, 0, 1),
				new AngularVelocity(0, 1),
				new Orientation(-Math.PI, true));
		Line facingLine = robot.getFacingLineBothWays();
		// Just back of
		Coord testPoint = new Coord(0.5 + robot.getHeight() / 2, 0.5);
		// Just in front
		Coord testPoint2 = new Coord(0.5 - robot.getHeight() / 2, 0.5);
		// The robot
		Coord testPoint3 = new Coord(0.5, 0.5);
		assertTrue(facingLine.contains(testPoint));
		assertTrue(facingLine.contains(testPoint2));
		assertTrue(facingLine.contains(testPoint3));
	}

	/**
	 * Given a robot, robot's facing line should contain a point that is just in
	 * front of it and just next to its back, and the robot itself. Different
	 * from the testFacingLineBothWays as the robot orientation is 180 degrees
	 * around
	 */
	@Test
	public void testFacingLineBothWaysOtherWayAround() {

		Robot robot = new Robot(new Coord(0.5, 0.5), new Velocity(0, 0, 1),
				new AngularVelocity(0, 1),
				new Orientation(0, true));
		Line facingLine = robot.getFacingLineBothWays();
		// Just in front
		Coord testPoint = new Coord(0.5 + robot.getHeight() / 2, 0.5);
		// Just back of
		Coord testPoint2 = new Coord(0.5 - robot.getHeight() / 2, 0.5);
		// The robot
		Coord testPoint3 = new Coord(0.5, 0.5);
		assertTrue(facingLine.contains(testPoint));
		assertTrue(facingLine.contains(testPoint2));
		assertTrue(facingLine.contains(testPoint3));
	}

	@Test
	public void testFindMaxRotationMaintainingPossession() {
		Robot r = new Robot(new Coord(0, 0), new Velocity(0, 0, 1),
				new AngularVelocity(0, 1),
				new Orientation(0, true));
		Ball b = new Ball(new Coord(0.12, 0.2), new Velocity(0, 0, 1));

		System.out.println(r.findMaxRotationMaintaintingPossession(b, true));
	}
}
