package balle.strategy.curve;

import balle.main.drawable.Drawable;
import balle.world.Coord;

public interface Curve extends Drawable {

	public abstract Coord pos(double t);

	public abstract Coord vel(double t);

	public abstract Coord acc(double t);

}
