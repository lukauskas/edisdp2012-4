package balle.world.objects;

import static org.junit.Assert.*;

import org.junit.Test;

import balle.world.Coord;
import balle.world.Line;
import balle.world.Velocity;


public class CircularObjectTests {
	
	@Test
	public void testIntersects(){
		
		/*
		 * Test 1: Expected result: true
		 */
		Line l = new Line (new Coord(0.0,0.0),  new Coord(0.5, 0.5));
		CircularObject circle = new CircularObject(new Coord(0.3, 0.3), new Velocity(0, 0, 1), 0.1);
		assertTrue(circle.intersects(l));
		
		/*
		 * Test 2: Expected result: false
		 */
		l = new Line (new Coord(0.4,0.5),  new Coord(0.5, 0.7));
		circle = new CircularObject(new Coord(0.8, 0.2), new Velocity(0, 0, 1), 0.1);
		assertFalse(circle.intersects(l));
		
	}

}
