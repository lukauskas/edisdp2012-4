package balle.world.objects;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import balle.world.Coord;
import balle.world.Line;
import balle.world.Velocity;

public class PitchTests {
	
	@Test
	public void testIntersects(){
		
		/*
		 * Test 1: Expected result: true
		 */
		Line l = new Line (new Coord(0.5,0.5),  new Coord(10.0, 10.0));
		Pitch p = new Pitch(0,2.44f,0,1.2f);
		assertTrue(p.intersects(l));
		
		/*
		 * Test 2: Expected result: false
		 */
		l = new Line (new Coord(0.4,0.5),  new Coord(0.5, 0.7));
		assertFalse(p.intersects(l));
		
	}


}
