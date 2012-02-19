package balle.world;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OrientationTest {

    /**
     * Given the angle in radians, both .radians() and .degrees() methods should
     * return correct angles
     */
    @Test
    public void checkThatRadiansInitialisationIsCorrect() {
        double radians = Math.PI / 2;
        Orientation a = new Orientation(radians, true);

        assertEquals(radians, a.radians(), 0.00001);
        assertEquals(90, a.degrees(), 0.00001);

        double radians2 = (7 * Math.PI) / 4;
        Orientation b = new Orientation(radians2, true);

        assertEquals(radians2, b.radians(), 0.00001);
        assertEquals(315, b.degrees(), 0.00001);

    }

    /**
     * Given the angle in degrees, both .radians() and .degrees() methods should
     * return correct angles
     */
    @Test
    public void checkThatDegreesInitialisationIsCorrect() {
        double degrees = 90;
        Orientation a = new Orientation(degrees, false);

        assertEquals(Math.PI / 2, a.radians(), 0.00001);
        assertEquals(degrees, a.degrees(), 0.00001);

        double degrees2 = 315;
        Orientation b = new Orientation(degrees2, false);

        assertEquals((3 * Math.PI / 2) + Math.PI / 4, b.radians(), 0.00001);
        assertEquals(degrees2, b.degrees(), 0.00001);

    }
}
