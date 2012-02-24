package balle.world.objects;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import sun.security.action.GetLongAction;

import balle.misc.Globals;
import balle.world.Coord;
import balle.world.Line;
import balle.world.Orientation;
import balle.world.Velocity;

public class RectangularObject extends MovingPoint implements FieldObject {
    
	private final double      width;
    private final double      height;
    private final Orientation orientation;
    
   public RectangularObject(Coord position, Velocity velocity, Orientation orientation,
            double width, double height) {
        super(position, velocity);
        this.width = width;
        this.height = height;
        this.orientation = orientation;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    @Override
    public boolean containsCoord(Coord point) {
        Coord dPoint = new Coord(point.getX()-getPosition().getX(),
        							point.getY()-getPosition().getY());
        
        dPoint = dPoint.rotate(getOrientation());
        if (dPoint.getX() < (-width/2.0f)) return false;
        if (dPoint.getX() > (width/2.0f)) return false;
        if (dPoint.getY() < (-height/2.0f)) return false;
        if (dPoint.getY() > (height/2.0f)) return false;
        return true;
    }

    @Override
    public boolean isNearWall(Pitch p) {
    	/*
    	 * Defines imaginary rectangle inside the pitch with size dependent on
    	 * the DISTANCE_TO_WALL constant. If the robot is within this rectangle
    	 * it is considered to be away from any walls.
    	 */
    	
    	double minX, maxX, minY, maxY;
    	minX = p.getMinX() + Globals.DISTANCE_TO_WALL;
    	maxX = p.getMaxX() - Globals.DISTANCE_TO_WALL;
    	minY = p.getMinY() + Globals.DISTANCE_TO_WALL;
    	maxY = p.getMaxY() - Globals.DISTANCE_TO_WALL;
    	
    	if (getPosition().getX() < minX)	return true;
    	if (getPosition().getX() > maxX)	return true;
    	if (getPosition().getY() < minY)	return true;
    	if (getPosition().getY() > maxY)	return true;
    	
    	return false;
    	
    }

    @Override
    public boolean isInCorner(Pitch p) {
        Coord c[] = new Coord[4];
        c[0] = new Coord(p.getMinX(), p.getMinY());
        c[1] = new Coord(p.getMinX(), p.getMaxY());
        c[2] = new Coord(p.getMaxX(), p.getMinY());
        c[3] = new Coord(p.getMaxX(), p.getMaxY());
        
        for (Coord each : c)
        	each = each.sub(getPosition());
        
        for (Coord each : c)
        	if (each.abs() < Globals.DISTANCE_TO_CORNER)
        		return true;
        
       return false;
    }

	@Override
	public boolean intersects(Line line) {
		if (containsCoord(line.getA()) || containsCoord(line.getB())) return true;
		
		Coord myA = line.getA();
		Coord myB = line.getB();
		Line2D l = new Line2D.Double(myA.getX(), myA.getY(), myB.getX(), myB.getY());

		double hw = getWidth()/2;
		double hh = getHeight()/2;

		Line a = new Line( hw,  hh,  hw, -hh);
		Line b = new Line( hw, -hh, -hw, -hh);
		Line c = new Line(-hw, -hh, -hw,  hh);
		Line d = new Line(-hw,  hh,  hw,  hh);
		Orientation o = getOrientation();
		a.rotate(o);
		b.rotate(o);
		c.rotate(o);
		d.rotate(o);
		Coord p = getPosition();
		a = a.add(p);
		b = b.add(p);
		c = c.add(p);
		d = d.add(p);
		Line2D a2D = new Line2D.Double(a.getA().getX(),  a.getA().getY(),  a.getB().getX(),  a.getB().getY());
		Line2D b2D = new Line2D.Double(b.getA().getX(),  b.getA().getY(),  b.getB().getX(),  b.getB().getY());
		Line2D c2D = new Line2D.Double(c.getA().getX(),  c.getA().getY(),  c.getB().getX(),  c.getB().getY());
		Line2D d2D = new Line2D.Double(d.getA().getX(),  d.getA().getY(),  d.getB().getX(),  d.getB().getY());
		
		// if the line l intersects any of the lines connecting the corners of
		// this rectangle, then return true;
		return 	l.intersectsLine(a2D) ||
				l.intersectsLine(b2D) ||
				l.intersectsLine(c2D) ||
				l.intersectsLine(d2D);
	}

}
