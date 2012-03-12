package balle.world;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LineTest {

	/**
	 * Given lines: (0,0) -> (1,0); (0,0) -> (0, 1); (0,0) -> (1,1) Lines should
	 * contain points (0.5,0), (0,0.5), (0.5,0.5) respectively.
	 * 
	 * All lines should also contain their end points
	 */
	@Test
	public void testContains() {
		Line l1 = new Line(0, 0, 1, 0);
		Line l2 = new Line(0, 0, 0, 1);
		Line l3 = new Line(0, 0, 1, 1);
		Line l4 = new Line(2.65, 0.3, 2.65, 0.9);

		assertTrue("l1 does not contain point (0.5, 0)",
				l1.contains(new Coord(0.5, 0)));
		assertTrue("l2 does not contain point (0, 0.5)",
				l2.contains(new Coord(0, 0.5)));
		assertTrue("l3 does not contain point (0.5, 0.5)",
				l3.contains(new Coord(0.5, 0.5)));

		assertTrue(
				"l4 does not contain (2.6499999999999995, 0.46083784792155075)",
				l4.contains(new Coord(2.6499999999999995, 0.46083784792155075)));

		// Check endpoints for l1
		assertTrue("l1 does not contain point (0, 0)",
				l1.contains(new Coord(0, 0)));
		assertTrue("l1 does not contain point (1, 0)",
				l1.contains(new Coord(1, 0)));
	}

	/**
	 * Given lines: (0,0) -> (1,0); (0,0) -> (0, 1); (0,0) -> (1,1) Lines should
	 * not contain points (1.5,0), (0,1.5), (1.5,0.5) respectively
	 */
	@Test
	public void testDoesNotContain() {
		Line l1 = new Line(0, 0, 1, 0);
		Line l2 = new Line(0, 0, 0, 1);
		Line l3 = new Line(0, 0, 1, 1);

		assertFalse("l1 does contain point (1.5, 0)",
				l1.contains(new Coord(1.5, 0)));
		assertFalse("l2 do contain point (0, 1.5)",
				l2.contains(new Coord(0, 1.5)));
		assertFalse("l3 does not contain point (1.5, 0.5)",
				l3.contains(new Coord(1.5, 0.5)));

	}

	@Test
	public void testGetIntersect() {
		Line l1 = new Line(0, 0, 10, 10);
		Line l2 = new Line(0, 10, 10, 0);
		Coord intersect = l1.getIntersect(l2);

		assertTrue("Intersection should not be null.", intersect != null);
		assertEquals("X should equal 5", 5, intersect.getX(), 0.00001);
		assertEquals("Y should equal 5", 5, intersect.getY(), 0.00001);
	}

	@Test
	public void testGetIntersect2() {
		Line l1 = new Line(1.4055631160736084, 0.5229264497756958,
				2.9036996161479287, 0.44818003083093333);
		Line l2 = new Line(2.65, 0.3, 2.65, 0.9);
		Coord intersect = l1.getIntersect(l2);
		assertTrue("Intersection should not be null.", intersect != null);
		assertEquals("X should equal 2.65", 2.65, intersect.getX(), 0.00001);
		assertEquals("Y should equal 0.460838", 0.460838, intersect.getY(),
				0.00001);
	}

	@Test
	public void testAngle() {
		Line l1 = new Line(0, 0, 5.23, 9.124);
		
		assertEquals("Incorrect line angle", 1.05031, l1.angle().radians(), 0.0001);
	}

	@Test
	public void testExtend() {
		Line l1 = new Line(2, 3, 4, 5);
		Line l2 = new Line(2, 3, 4.7071068, 5.7071068);
		Line l3 = new Line(2, 3, 7.5355339, 8.5355339);
		
		assertEquals("Line should end at (4.7071068, 5.7071068)", l2,
				l1.extend(1));
		assertEquals("Line should end at (7.5355339, 8.5355339)", l3,
				l1.extend(5));

		Line l4 = new Line(0, 0, 5, 0);
		Line l5 = new Line(0, 0, 7.6, 0);

		assertEquals("Line should end at (7, 0)", l5, l4.extend(2.6));

        assertEquals("Line should end at (4, 5)", l1, l1.extend(0));

	}
}
