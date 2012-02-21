package balle.world.objects;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import balle.world.Coord;
import balle.world.Line;
import balle.world.Velocity;

public class GoalTests {
	
	@Test
	public void testIntersects(){
		
		/*
		 * Test 1: Expected result: false
		 */
		Line l = new Line (new Coord(0.0,0.0),  new Coord(0.5, 0.5));
		Goal g = new Goal(true, 0.0, 0.4, 0.4, 0.8);
		assertFalse(g.intersects(l));
		
		/*
		 * Test 2: Expected result: true
		 */
		l = new Line (new Coord(0.4,0.5),  new Coord(0.5, 0.7));
		g = new Goal(true, 0.0, 0.4, 0.4, 0.8);
		assertTrue(g.intersects(l));
		
	}


}
