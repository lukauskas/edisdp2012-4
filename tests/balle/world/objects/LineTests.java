package balle.world.objects;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import balle.world.Coord;
import balle.world.Line;

public class LineTests {
	
	@Test
	public void testIntersectionPoint(){
		Line a = new Line (new Coord(0,0),  new Coord(2, 2));
		Line b = new Line (new Coord(0,2),  new Coord(2, 0));
		
		Coord intersect = a.getIntersect(b);
		assertEquals(new Coord(1,1), intersect);
	}

    @Test
    public void testClosestPoint() {
        Line a = new Line(new Coord(0, 0), new Coord(2, 2));
        Coord p = new Coord(2,0);
        Coord closestPoint = a.closestPoint(p);

        assertEquals(new Coord(1, 1).getX(), closestPoint.getX(), 0.00001);
        assertEquals(new Coord(1, 1).getY(), closestPoint.getY(), 0.00001);

    }


}
