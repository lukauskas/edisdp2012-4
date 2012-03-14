package balle.world.objects;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import balle.world.AngularVelocity;
import balle.world.Coord;
import balle.world.Line;
import balle.world.Orientation;
import balle.world.Velocity;

public class RectangularObjectTest {

	@Test
	public void testIsNearWall() {
		
		Pitch p = new Pitch(0,2.44f,0,1.2f);
		Robot robot = new Robot(new Coord(0.05, 0.05), new Velocity(0, 0, 1),
				new AngularVelocity(0, 0), new Orientation(0.2, true));

		/*
		 * Test with robot coordinates at the centre -> the test should return
		 * false
		 */
		assertTrue(robot.isNearWall(p));
			
		/*
		 * Test with robot coordinates (0.1, 0.1) -> the test should return true.
		 */
		
		robot = new Robot(new Coord(0.7, 0.55), new Velocity(0, 0, 1),
				new AngularVelocity(0, 0), new Orientation(0.2, true));

		assertFalse(robot.isNearWall(p));
		
	}
	
	@Test
	public void testIntersects() {
		RectangularObject r = new RectangularObject(new Coord(1.2f, 0.5f),
				new Velocity(new Coord(0, 0), 1), new AngularVelocity(0, 0),
						new Orientation(0,true), 0.2, 0.2);
		Line line1 = new Line(new Coord(0.1f,0.5f), new Coord(2f,0.5f));
		
		assertTrue(r.intersects(line1));
	}

	@Test
	public void testDoesNotIntersects() {
		RectangularObject r = new RectangularObject(new Coord(1.2f, 0.5f),
				new Velocity(new Coord(0, 0), 1), new AngularVelocity(0, 0),
				new Orientation(0,true), 0.2, 0.2);
		Line line2 = new Line(new Coord(0.1f,0f), new Coord(2f,0f));
		assertFalse(r.intersects(line2));
	}

}
