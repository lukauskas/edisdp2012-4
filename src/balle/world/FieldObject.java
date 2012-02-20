package balle.world;

public interface FieldObject {

    /**
     * Gets the coordinate of the centroid of the object
     * 
     * @return the position
     */
    public abstract Coord getPosition();

    /**
     * Gets the velocity of the object (centroid to centroid)
     * 
     * @return the velocity
     */
    public abstract Velocity getVelocity();

    /**
     * Returns true if point is contained in the object
     * 
     * @param point
     *            the point in question
     * @return true if point is in the object
     */
    public abstract boolean containsCoord(Coord point);

}