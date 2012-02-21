package balle.world.objects;

import balle.misc.Globals;
import balle.world.Coord;
import balle.world.Velocity;

public class CircularObject extends MovingPoint implements FieldObject {

    private final double radius;

    public CircularObject(Coord position, Velocity velocity, double radius) {
        super(position, velocity);
        this.radius = radius;
    }

    @Override
    public boolean containsCoord(Coord point) {
        return getPosition().dist(point) <= getRadius();
    }

    public double getRadius() {
        return radius;
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
}
