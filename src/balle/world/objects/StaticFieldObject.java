package balle.world.objects;

import balle.world.Coord;
import balle.world.Line;

public interface StaticFieldObject {

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
