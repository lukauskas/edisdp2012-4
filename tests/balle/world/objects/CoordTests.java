package balle.world.objects;

import static org.junit.Assert.*;

import org.junit.*;
import balle.world.Coord;

public class CoordTests {
	
	public Pitch pitch;
	
	@Before
	public void init() {
		pitch = new Pitch(0,2.44f,0,1.2f);
	}
	
	/** Given 2 different lines, verify that the testIsReachableInStraightLineAndNotBlocked correctly
	 *  identifies whether the lines are obstructed.
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
		
		//Coord 
		//FieldObject robot = new Robot()
	}

}
