package balle.world.objects;

import balle.world.Coord;
import balle.world.Line;
import balle.world.Velocity;

public abstract class StaticFieldObject implements FieldObject {

    /**
     * Gets the coordinate of the centroid of the object
     * 
     * @return the position
     */
    public abstract Coord getPosition();

    /**
     * Returns true if point is contained in the object
     * 
     * @param point
     *            the point in question
     * @return true if point is in the object
     */
    public abstract boolean containsCoord(Coord point);
    
    
    public abstract boolean intersects(Line line);

    /**
     * Gets the velocity of the object (centroid to centroid)
     * 
     * @return the velocity
     */
    public Velocity getVelocity() {
        return new Velocity(0, 0, 1);
    }

    /**
     * Checks if object is near a wall;
     * 
     * @return true, if the object is indeed near the wall
     */
    public boolean isNearWall(Pitch p) {
        // TODO: implement
        System.err.println("StaticFieldObject.isNearWall not implemented");
        return false;
    }

    /**
     * Checks if an object is in a corner
     * 
     * @return true, if is in corner
     */
    public boolean isInCorner(Pitch p) {
        // TODO: implement
        System.err.println("StaticFieldObject.isInCorner not implemented");
        return false;
    }

}
