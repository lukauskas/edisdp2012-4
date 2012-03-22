package balle.misc;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GlobalsTest {

	@Test
	public void testP2V() {
		float x = 725;
		assertEquals(Globals.velocityToPower(Globals.powerToVelocity(x)), x,
				0.0001);

		for (int i = 0; i <= Globals.MAXIMUM_MOTOR_SPEED; i += 1) {
			assertEquals(Globals.velocityToPower(Globals.powerToVelocity(i)),
					i, 0.5);
		}
	}

	@Test
	public void testV2P() {
		float x = 0.05457f;
		assertEquals(Globals.powerToVelocity(Globals.velocityToPower(x)), x,
				0.000001);

		float max = Globals.powerToVelocity(Globals.MAXIMUM_MOTOR_SPEED);
		float step = max / 50;
		for (float i = max; i >= 0; i -= step) {
			assertEquals(Globals.powerToVelocity(Globals.velocityToPower(i)),
					i, 0.5);
		}
	}

}
