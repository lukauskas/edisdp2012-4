package balle.world.objects;

import balle.world.Velocity;

public interface FieldObject extends StaticFieldObject {

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
    public abstract boolean isNearWall();

    /**
     * Checks if an object is in a corner
     * 
     * @return true, if is in corner
     */
    public abstract boolean isInCorner();

}