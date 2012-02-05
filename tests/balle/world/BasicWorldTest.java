package balle.world;

import org.junit.*;
import static org.junit.Assert.*;

import balle.world.BasicWorld;
import balle.world.Coord;
import balle.world.Robot;


public class BasicWorldTest {

	public BasicWorld worldB;	// balle is blue
	public BasicWorld worldY;	// balle is yellow
	
	/**
	 * setup the two BasicWorlds
	 */
	@Before public void init() {
		worldB = new BasicWorld(true);
		worldY = new BasicWorld(false);
	}
	
	/**
	 * Assert that after an update, the snapshot returned
	 * has the BALL position passed to the update method.
	 */
	@Test public void testBallPositionCorrect() {
		double x = 0.2;
		double y = 0.4;
		worldB.update(0, 0, 0, 0, 0, 0, x, y, 0);
		
		Coord ballPos = worldB.getSnapshot().getBall().getPosition();
		assertEquals(x, ballPos.getX(), 0);
		assertEquals(x, ballPos.getX(), 0);
	}
	
	/**
	 * Assert that World properly abstracts away the color of each
	 * robot
	 */
	@Test public void testRobotColorAbstraction() {
		
		double yellowX = 0.4;
		double yellowY = 0.5;
		double yellowA = 1;
		
		double blueX   = 0.2;
		double blueY   = 0.3;
		double blueA   = 2;

		worldB.update(yellowX, yellowY, yellowA, blueX, blueY, blueA, 0, 0, 0);
		worldY.update(yellowX, yellowY, yellowA, blueX, blueY, blueA, 0, 0, 0);

		// check when balle is yellow
		Robot balleY = worldY.getSnapshot().getBalle();
		assertEquals(yellowX, balleY.getPosition().getX(), 0);
		assertEquals(yellowY, balleY.getPosition().getY(), 0);
		assertEquals(yellowA, balleY.getOrientation(), 0);

		Robot opponentB = worldY.getSnapshot().getOpponent();
		assertEquals(blueX, opponentB.getPosition().getX(), 0);
		assertEquals(blueY, opponentB.getPosition().getY(), 0);
		assertEquals(blueA, opponentB.getOrientation(), 0);
		
		// check when balle is blue 
		Robot balleB = worldB.getSnapshot().getBalle();
		assertEquals(blueX, balleB.getPosition().getX(), 0);
		assertEquals(blueY, balleB.getPosition().getY(), 0);
		assertEquals(blueA, balleB.getOrientation(), 0);

		Robot opponentY = worldB.getSnapshot().getOpponent();
		assertEquals(yellowX, opponentY.getPosition().getX(), 0);
		assertEquals(yellowY, opponentY.getPosition().getY(), 0);
		assertEquals(yellowA, opponentY.getOrientation(), 0);
	}
	
	/**
	 * Test estimated velocities
	 */
	@Test public void testVelocityProperlyEstimated() {
		double dX = 1.5;
		long dT = 1;

		worldY.update(0, 0, 0, 0, 0, 0, 0, 0, 0);
		worldY.update(dX, 0, 0, 0, 0, 0, 0, 0, dT);

		assertEquals(1.5, worldY.getSnapshot().getBalle().getVelocity().abs(), 0.0000001);
	}
	
	/**
	 * Test estimated velocities at an angle
	 */
	@Test public void testVelocityProperlyEstimatedAtAnAngle() {
		double dX = 3;
		double dY = 4;
		long dT = 1;

		worldY.update(0, 0, 0, 0, 0, 0, 0, 0, 0);
		worldY.update(dX, dY, 0, 0, 0, 0, 0, 0, dT);

		assertEquals(5, worldY.getSnapshot().getBalle().getVelocity().abs(), 0.0000001);
	}
}
