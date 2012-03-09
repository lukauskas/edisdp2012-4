package balle.world.objects;

import balle.world.Coord;
import balle.world.Line;
import balle.world.Velocity;

public interface FieldObject {

    /**
     * Gets the velocity of the object (centroid to centroid)
     * 
     * @return the velocity
     */
    public abstract Velocity getVelocity();

    /**
     * Checks if object is near a wall;
     * 
     * @return true, if the object is indeed near the wall
     */
    public abstract boolean isNearWall(Pitch p);

    /**
     * Checks if an object is in a corner
     * 
     * @return true, if is in corner
     */
    public abstract boolean isInCorner(Pitch p);

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

}