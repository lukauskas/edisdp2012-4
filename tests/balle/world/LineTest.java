package balle.world;

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

        assertTrue("l1 does not contain point (0.5, 0)", l1.contains(new Coord(0.5, 0)));
        assertTrue("l2 does not contain point (0, 0.5)", l2.contains(new Coord(0, 0.5)));
        assertTrue("l3 does not contain point (0.5, 0.5)", l3.contains(new Coord(0.5, 0.5)));

        // Check endpoints for l1
        assertTrue("l1 does not contain point (0, 0)", l1.contains(new Coord(0, 0)));
        assertTrue("l1 does not contain point (1, 0)", l1.contains(new Coord(1, 0)));
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

        assertFalse("l1 does contain point (1.5, 0)", l1.contains(new Coord(1.5, 0)));
        assertFalse("l2 do contain point (0, 1.5)", l2.contains(new Coord(0, 1.5)));
        assertFalse("l3 does not contain point (1.5, 0.5)", l3.contains(new Coord(1.5, 0.5)));

    }
}
