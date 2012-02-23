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
     * Given coordinates (1,0) and angle of pi/2 radians, rotate function should
     * transform the coordinates to (0,1)
     */
    @Test
    public void testRotate() {
        Coord c = new Coord(1, 0);
        Coord newCoord = c.rotate(new Orientation(Math.PI / 2, true));
        assertEquals(0, newCoord.getX(), 0.000001);
        assertEquals(1, newCoord.getY(), 0.000001);
    }

}
