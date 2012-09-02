package balle.strategy.curve;

import balle.main.drawable.Drawable;
import balle.world.Coord;

public interface Curve extends Drawable {

	// Basic \\
	
	/** Shows position of a line at a moment in "time".
	 * 
	 * @param t
	 * @return
	 */
	public abstract Coord pos(double t);
	
	/** First differential of position, shows "speed" of line
	 * at a given moment in "time". 
	 * 
	 * @param t
	 * @return
	 */
	public abstract Coord vel(double t);
	
	/** Second differential of position, shows "acceleration" of the 
	 * line at a given moment in "time".
	 * 
	 * @param t
	 * @return
	 */
	public abstract Coord acc(double t);

	// Centre of Rotation \\
	
	/**
	 * Centre Of Rotation.
	 * 
	 * Find the centre point of the circle that fits this section of the curve.
	 * 
	 * @param t
	 *            The point in time.
	 * @return The distance to the circles centre point, orthogonal to the
	 *         curve. (Left or right will be negative)
	 */
	public abstract Coord cor(double t);

	/**
	 * 
	 * @param t
	 * @return Radius of the centre of rotation.
	 */
	public abstract double rad(double t);
	
	
	// Utility \\
	
	public abstract Coord getStart();
	
	public abstract Coord getEnd();

	public abstract Coord closestPoint(Coord c);

	public abstract double length();

}
