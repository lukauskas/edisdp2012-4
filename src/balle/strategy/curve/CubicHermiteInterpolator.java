package balle.strategy.curve;

import balle.world.Coord;
import balle.world.Orientation;

public abstract class CubicHermiteInterpolator implements Interpolator {

	@Override
	public Curve getCurve(Coord[] controlPoints, Orientation start,
			Orientation end) {
		return new Spline(getCurves(controlPoints, start, end));
	}

	public abstract Bezier4[] getCurves(Coord[] controlPoints,
			Orientation start, Orientation end);

}
