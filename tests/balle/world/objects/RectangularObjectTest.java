package balle.world.objects;

import java.awt.Point;
import java.awt.Rectangle;

import org.junit.Test;

import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Velocity;

import static org.junit.Assert.*;

public class RectangularObjectTest {

	private final static double DISTANCE_TO_WALL = 0.1;
	private final static double DISTANCE_TO_CORNER = 0.2;

	@Test
	public void testIsNearWall() {
		
		Pitch p = new Pitch(0,2.44f,0,1.2f);
		Robot robot = new Robot(new Coord(0.05, 0.05), new Velocity(0, 0, 1), new Orientation(0.2, true));

		/*
		 * Test with robot coordinates at the centre -> the test should return
		 * false
		 */
		System.out.println(robot.getPosition());
		assertTrue(robot.isNearWall(p));
			
		/*
		 * Test with robot coordinates (0.1, 0.1) -> the test should return true.
		 */
		
		robot = new Robot(new Coord(0.7, 0.55), new Velocity(0, 0, 1), new Orientation(0.2, true));

		assertFalse(robot.isNearWall(p));
		
	}

}
