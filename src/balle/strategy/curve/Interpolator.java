package balle.strategy.curve;

import balle.world.Coord;

public interface Interpolator {

	public abstract Curve getCurve(Coord[] controlPoints);

}
