package balle.world.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import balle.world.Coord;
import balle.world.Orientation;

public class CoordTests {

	public Pitch pitch;

	@Before
	public void init() {
		pitch = new Pitch(0, 2.44f, 0, 1.2f);
	}

	/**
	 * Given 2 different lines, verify that the
	 * testIsReachableInStraightLineAndNotBlocked correctly identifies whether
	 * the lines are obstructed.
	 */
	@Test
	public void testIsReachableInStraightLineAndNotBlocked() {
		Coord start, finish;
		start = new Coord(0.5f, 0.5f);
		finish = new Coord(2.0f, 0.5f);
		assertTrue("Incorrectly identified obsticle",
				start.isReachableInStraightLineAndNotBlocked(finish, pitch));

		start = new Coord(0.5f, -0.5f);
		finish = new Coord(0.5f, 0.5f);
		assertFalse("Didn't detect pitch wall",
				start.isReachableInStraightLineAndNotBlocked(finish, pitch));
	}

	public void testIsReachableInStraightLineAndNotBlocked2() {

		// Coord
		// FieldObject robot = new Robot()
	}

	/**
	 * Given coordinates (1,0) and appropriate angle, the rotate function should
	 * rotate the coordinates correctly.
	 */
	@Test
	public void testRotate() {
		Coord c = new Coord(1, 0);

		// rotate((1,0), PI/2) = (0,1)
		Coord newCoord = c.rotate(new Orientation(Math.PI / 2, true));
		assertEquals(0, newCoord.getX(), 0.000001);
		assertEquals(1, newCoord.getY(), 0.000001);

		// rotate((1,0), PI) = (-1,0)
		Coord newCoord2 = c.rotate(new Orientation(Math.PI, true));
		assertEquals(-1, newCoord2.getX(), 0.000001);
		assertEquals(0, newCoord2.getY(), 0.000001);

		// rotate((1,0), 3/2 * PI) = (0,-1)
		Coord newCoord3 = c.rotate(new Orientation((3 * Math.PI) / 2, true));
		assertEquals(0, newCoord3.getX(), 0.000001);
		assertEquals(-1, newCoord3.getY(), 0.000001);

		// rotate((1,0), 2 * PI) = (1,0)
		Coord newCoord4 = c.rotate(new Orientation(2 * Math.PI, true));
		assertEquals(1, newCoord4.getX(), 0.000001);
		assertEquals(0, newCoord4.getY(), 0.000001);
	}

	@Test
	public void testAngleBetween() {
		Orientation out;
		Coord a, b, c;
		a = new Coord(0, 0);
		b = new Coord(3, 0);
		c = new Coord(3, 3);

		out = a.angleBetween(c, b);
		assertEquals(-45, out.atan2styledegrees(), 0.001);

		out = a.angleBetween(b, c);
		assertEquals(45, out.atan2styledegrees(), 0.001);

		out = b.angleBetween(c, a);
		assertEquals(90, out.atan2styledegrees(), 0.001);
	}

	/**
	 * Given lines: (0,0) -> (1,0); (0,0) -> (0, 1); (0,0) -> (1,1) Lines should
	 * contain points (0.5,0), (0,0.5), (0.5,0.5) respectively.
	 * 
	 * All lines should also contain their end points
	 */
	@Test
	public void testAngleBetween2() {
		Coord ref = new Coord(1, 0);
		Coord from = new Coord(1, 1);
		Coord to = new Coord(0, 1);
		assertEquals(Math.PI / 4, ref.angleBetween(from, to).radians(), 0.0001);
		assertEquals(Math.PI / 4, ref.angleBetween(new Coord(1, 100), to)
				.radians(), 0.0001);
	}

	@Test
	public void testOrientationConstructor() {
		for (double t = -2 * Math.PI; t < 2 * Math.PI; t += 0.1) {
			assertTrue(new Orientation(t).radians() >= 0);
		}
	}
}
