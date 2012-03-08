package balle.misc;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GlobalsTest {

	@Test
	public void testP2V() {
		float x = 725;
		assertEquals(Globals.velocityToPower(Globals.powerToVelocity(x)), x,
				0.0001);
	}

	@Test
	public void testV2P() {
		float x = 0.05457f;
		assertEquals(Globals.powerToVelocity(Globals.velocityToPower(x)), x,
				0.000001);
	}

}
