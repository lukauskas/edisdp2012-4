package balle.strategy.curve;

import balle.main.drawable.Drawable;
import balle.world.Coord;

public interface Curve extends Drawable {

	public abstract Coord pos(double t);

	public abstract Coord vel(double t);

	public abstract Coord acc(double t);

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
}
