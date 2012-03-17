package balle.world;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import balle.misc.Globals;

class AbstractWorldConcrete extends AbstractWorld {

	public AbstractWorldConcrete(boolean isBalleBlue, boolean goalIsLeft) {
		super(isBalleBlue, goalIsLeft, Globals.getPitch());
	}

	@Override
	public Snapshot getSnapshot() {
		// TODO mock this!
		return null;
	}

	@Override
	protected void updateScaled(Coord yellowPos, Orientation yellowOrientation,
			Coord bluePos, Orientation blueOrientation, Coord ballPos,
			long timestamp) {
		// TODO Auto-generated method stub

	}

	public Snapshot estimateAt(long t) {
		return null;
	}

}

public class AbstractWorldTest {

	/**
	 * Given the coordinate system that is twice as big as our pitch, all
	 * coordinates should be scaled halfway
	 */
	@Test
	public void testScaleXToMeters() {
		AbstractWorld world = new AbstractWorldConcrete(true, true);

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
		AbstractWorld world = new AbstractWorldConcrete(true, true);

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
		AbstractWorld world = spy(new AbstractWorldConcrete(false, true));
		// Both coordinates twice as big so it's easy to scale
		world.updatePitchSize(Globals.PITCH_WIDTH * 2, Globals.PITCH_HEIGHT * 2);
		world.update(0.2, 0.4, 0, 0.6, 0.8, 0, 1, 1.2, 0);
		// Verify that update scaled is called correctly
		verify(world).updateScaled(new Coord(0.1, 0.2),
				new Orientation(0, false), new Coord(0.3, 0.4),
				new Orientation(0, false), new Coord(0.5, 0.6), 0);

		// Flip robot colour here
		AbstractWorld world2 = spy(new AbstractWorldConcrete(true, true));
		// Both coordinates twice as big so it's easy to scale
		world2.updatePitchSize(Globals.PITCH_WIDTH * 2,
				Globals.PITCH_HEIGHT * 2);
		world2.update(0.2, 0.4, 0, 0.6, 0.8, 0, 1, 1.2, 0);
		// Verify that update scaled is called correctly
		verify(world2).updateScaled(new Coord(0.3, 0.4),
				new Orientation(0, false), new Coord(0.1, 0.2),
				new Orientation(0, false), new Coord(0.5, 0.6), 0);
	}

	/**
	 * Given an AbstractWorld class that has the pitch size initialised
	 * correctly, and null for coordinates to update the state of the world to
	 * the AbstractWorld class should send nulls for *both* the coordinate and
	 * orientation objects.s
	 */
	@Test
	public void updateCallsUpdateScaledWithNullParameters() {
		AbstractWorld world = spy(new AbstractWorldConcrete(false, true));
		// Both coordinates twice as big so it's easy to scale
		world.updatePitchSize(Globals.PITCH_WIDTH * 2, Globals.PITCH_HEIGHT * 2);
		world.update(-1, -1, 15, -1, -1, 0, -1, -1, 0);
		// Verify that update scaled is called correctly
		verify(world).updateScaled(null, null, null, null, null, 0);

		// Flip robot colour here!
		AbstractWorld world2 = spy(new AbstractWorldConcrete(true, true));
		// Both coordinates twice as big so it's easy to scale
		world2.updatePitchSize(Globals.PITCH_WIDTH * 2,
				Globals.PITCH_HEIGHT * 2);
		world2.update(-1, -1, 15, -1, -1, 0, -1, -1, 0);
		// Verify that update scaled is called correctly
		verify(world2).updateScaled(null, null, null, null, null, 0);
	}
}
