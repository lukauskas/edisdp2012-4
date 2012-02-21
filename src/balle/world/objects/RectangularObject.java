package balle.world.objects;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Velocity;

public class RectangularObject extends MovingPoint implements FieldObject {
    
	private final static double DISTANCE_TO_WALL = 0.1;
	private final static double DISTANCE_TO_CORNER = 0.2;
	
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
    	minX = p.getMinX() + DISTANCE_TO_WALL;
    	maxX = p.getMaxX() - DISTANCE_TO_WALL;
    	minY = p.getMinY() + DISTANCE_TO_WALL;
    	maxY = p.getMaxY() - DISTANCE_TO_WALL;
    	
    	if (getPosition().getX() < minX)	return true;
    	if (getPosition().getX() > maxX)	return true;
    	if (getPosition().getY() < minY)	return true;
    	if (getPosition().getY() > maxY)	return true;
    	
    	return false;
    	
    }

    @Override
    public boolean isInCorner() {
        // TODO Auto-generated method stub
        return false;
    }

}
