package balle.strategy.curve;

import balle.world.Coord;
import balle.world.Orientation;

public interface Interpolator {

	public abstract Spline getCurve(Coord[] controlPoints, Orientation start,
			Orientation end);

}
