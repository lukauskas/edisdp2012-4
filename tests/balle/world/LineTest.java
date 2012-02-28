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

		assertTrue("l1 does not contain point (0.5, 0)",
				l1.contains(new Coord(0.5, 0)));
		assertTrue("l2 does not contain point (0, 0.5)",
				l2.contains(new Coord(0, 0.5)));
		assertTrue("l3 does not contain point (0.5, 0.5)",
				l3.contains(new Coord(0.5, 0.5)));

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
	public void testGetIntersectBug() {
		Line l1 = new Line(1.4055631160736084, 0.5229264497756958,
				2.9036996161479287, 0.44818003083093333);
		Line l2 = new Line(2.65, 0.3, 2.65, 0.9);
		Coord intersect = l1.getIntersect(l2);
		assertTrue("Intersection should not be null.", intersect != null);
		assertEquals("X should equal 2.65", 2.65, intersect.getX(), 0.00001);
		// assertEquals("Y should equal 0.273779", 0.273779, intersect.getY(),
		// 0.00001);
	}
}
