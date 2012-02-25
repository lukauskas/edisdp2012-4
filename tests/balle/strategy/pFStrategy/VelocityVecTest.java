package balle.strategy.pFStrategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class VelocityVecTest {

    /**
     * Given a velocity vector that is (-1011, -815) (converted to radians), the
     * scale method of VelocityVec should reduce the vector so the individual
     * wheel speeds are < MAX_SPEED when converted to degrees
     */
    @Test
    public void testScale() {
        VelocityVec v = new VelocityVec(Math.toRadians(-1011),
                Math.toRadians(-815));

        VelocityVec newVec = v.scale();

        assertEquals(-VelocityVec.MAX_SPEED, Math.toDegrees(newVec.getLeft()),
                0.00001);
        assertTrue(
                Math.abs(Math.toDegrees(newVec.getRight())) + " > "
                        + VelocityVec.MAX_SPEED,
                Math.abs(Math.toDegrees(newVec.getRight())) <= VelocityVec.MAX_SPEED);

        VelocityVec v2 = new VelocityVec(Math.toRadians(815),
                Math.toRadians(1009));

        VelocityVec newVec2 = v2.scale();

        assertTrue(
                Math.abs(Math.toDegrees(newVec2.getLeft())) + " > "
                        + VelocityVec.MAX_SPEED,
                Math.abs(Math.toDegrees(newVec2.getLeft())) <= VelocityVec.MAX_SPEED);
        assertEquals(VelocityVec.MAX_SPEED, Math.toDegrees(newVec2.getRight()),
                0.00001);
    }
}
