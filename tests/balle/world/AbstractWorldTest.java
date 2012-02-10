package balle.world;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import balle.misc.Globals;

class AbstractWorldConcrete extends AbstractWorld {

    public AbstractWorldConcrete(boolean isBalleBlue) {
        super(isBalleBlue);
    }

    @Override
    public Snapshot getSnapshot() {
        // TODO mock this!
        return null;
    }

    @Override
    protected void updateScaled(double yPosX, double yPosY, double yRad,
            double bPosX, double bPosY, double bRad, double ballPosX,
            double ballPosY, long timestamp) {
        // TODO mock this

    }

}

public class AbstractWorldTest {

    /**
     * Given the coordinate system that is twice as big as our pitch, all
     * coordinates should be scaled halfway
     */
    @Test
    public void testScaleXToMeters() {
        AbstractWorld world = new AbstractWorldConcrete(true);

        world.updatePitchSize(Globals.PITCH_WIDTH * 2, Globals.PITCH_HEIGHT);
        assertEquals(0.5, world.scaleXToMeters(1), 0.0001);
        assertEquals(0, world.scaleXToMeters(0), 0.0001);
        assertEquals(Globals.PITCH_WIDTH,
                world.scaleXToMeters(Globals.PITCH_WIDTH * 2), 0.0001);

    }

    /**
     * Given the coordinate system that is twice as big as our pitch, all
     * coordinates should be scaled halfway
     */
    @Test
    public void testScaleYToMeters() {
        AbstractWorld world = new AbstractWorldConcrete(true);

        world.updatePitchSize(Globals.PITCH_WIDTH, Globals.PITCH_HEIGHT * 2);
        assertEquals(0.5, world.scaleYToMeters(1), 0.0001);
        assertEquals(0, world.scaleYToMeters(0), 0.0001);
        assertEquals(Globals.PITCH_HEIGHT,
                world.scaleYToMeters(Globals.PITCH_HEIGHT * 2), 0.0001);

    }

    /**
     * Given an AbstractWorld class that has the pitch size initialised
     * correctly, and coordinates to update the world state with it should call
     * updateScaled function with the exact same coordinates scaled
     */
    @Test
    public void updateCallsUpdateScaledWithCorrectParameters() {
        AbstractWorld world = spy(new AbstractWorldConcrete(true));
        // Both coordinates twice as big so it's easy to scale
        world.updatePitchSize(Globals.PITCH_WIDTH * 2, Globals.PITCH_HEIGHT * 2);
        world.update(0.2, 0.4, 0, 0.6, 0.8, 0, 1, 1.2, 0);
        // Verify that update scaled is called correctly
        verify(world).updateScaled(0.1, 0.2, 0, 0.3, 0.4, 0, 0.5, 0.6, 0);
    }
}
