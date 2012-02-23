package balle.world;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import balle.misc.Globals;
import balle.world.objects.Robot;

public class BasicWorldTest {

	public BasicWorld worldB; // balle is blue
	public BasicWorld worldY; // balle is yellow

	/**
	 * setup the two BasicWorlds
	 */
	@Before
	public void init() {
		worldB = new BasicWorld(true, true, Globals.getPitch());
		worldB.updatePitchSize(Globals.PITCH_WIDTH, Globals.PITCH_HEIGHT);
		worldY = new BasicWorld(false, true, Globals.getPitch());
		worldY.updatePitchSize(Globals.PITCH_WIDTH, Globals.PITCH_HEIGHT);
	}

	/**
	 * Given a world object that was just initialised the snapshot should be an
	 * empty one initially
	 */
	@Test
	public void testSnapshotIsEmptySnapshotInitially() {
		BasicWorld world = new BasicWorld(true, true, Globals.getPitch());
		assertTrue(EmptySnapshot.class.isInstance(world.getSnapshot()));
	}

	/**
	 * Given a world object that had its pitch size just updated the Snapshot
	 * should be null initially
	 */
	@Test
	public void testSnapshotIsEmptyAfterPitchSizeUpdate() {
		worldB.update(0.3, 0.3, 30, 0.5, 0.3, 13, 0.5, 0.5,
				System.currentTimeMillis()); // generate a new
		worldB.updatePitchSize(10, 10); // update pitch size
		assertTrue(EmptySnapshot.class.isInstance(worldB.getSnapshot()));
	}

	/**
	 * Assert that after an update, the snapshot returned has the BALL position
	 * passed to the update method.
	 */
	@Test
	public void testBallPositionCorrect() {
		double x = 0.2;
		double y = 0.4;
		worldB.update(0, 0, 0, 0, 0, 0, x, y, 0);

		Coord ballPos = worldB.getSnapshot().getBall().getPosition();
		assertEquals(x, ballPos.getX(), 0.00001);
		assertEquals(x, ballPos.getX(), 0.00001);
	}

	/**
	 * Assert that World properly abstracts away the color of each robot
	 */
	@Test
	public void testRobotColorAbstraction() {

		double yellowX = 0.4;
		double yellowY = 0.5;
		double yellowA = 90;

		double blueX = 0.2;
		double blueY = 0.3;
		double blueA = 180;

		worldB.update(yellowX, yellowY, yellowA, blueX, blueY, blueA, 0, 0, 0);
		worldY.update(yellowX, yellowY, yellowA, blueX, blueY, blueA, 0, 0, 0);

		// check when balle is yellow
		Robot balleY = worldY.getSnapshot().getBalle();
		assertEquals(yellowX, balleY.getPosition().getX(), 0);
		assertEquals(yellowY, balleY.getPosition().getY(), 0);
		assertEquals(yellowA, balleY.getOrientation().degrees(), 0);

		Robot opponentB = worldY.getSnapshot().getOpponent();
		assertEquals(blueX, opponentB.getPosition().getX(), 0);
		assertEquals(blueY, opponentB.getPosition().getY(), 0);
		assertEquals(blueA, opponentB.getOrientation().degrees(), 0);

		// check when balle is blue
		Robot balleB = worldB.getSnapshot().getBalle();
		assertEquals(blueX, balleB.getPosition().getX(), 0);
		assertEquals(blueY, balleB.getPosition().getY(), 0);
		assertEquals(blueA, balleB.getOrientation().degrees(), 0);

		Robot opponentY = worldB.getSnapshot().getOpponent();
		assertEquals(yellowX, opponentY.getPosition().getX(), 0);
		assertEquals(yellowY, opponentY.getPosition().getY(), 0);
		assertEquals(yellowA, opponentY.getOrientation().degrees(), 0);
	}

	/**
	 * Test estimated velocities
	 */
	@Test
	public void testVelocityProperlyEstimated() {
		double dX = 0.3; // Should be low enough not to get discarded
		long dT = 1;

		worldY.update(0, 0, 0, 0, 0, 0, 0, 0, 0);
		worldY.update(dX, 0, 0, 0, 0, 0, 0, 0, dT);

		assertEquals(dX, worldY.getSnapshot().getBalle().getVelocity().abs(),
				0.0000001);
	}

	/**
	 * Test estimated velocities at an angle
	 */
	@Test
	public void testVelocityProperlyEstimatedAtAnAngle() {
		double dX = 0.3; // Should be low enough not to get discarded
		double dY = 0.4; // Should be low enough not to get discarded
		long dT = 1;

		worldY.update(0, 0, 0, 0, 0, 0, 0, 0, 0);
		worldY.update(dX, dY, 0, 0, 0, 0, 0, 0, dT);

		assertEquals(0.5, worldY.getSnapshot().getBalle().getVelocity().abs(),
				0.0000001);
	}
}
