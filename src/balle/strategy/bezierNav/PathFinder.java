package balle.strategy.bezierNav;

import balle.strategy.curve.Interpolator;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Snapshot;

public interface PathFinder {

	public Coord[] getPath(Snapshot s, Coord start, Orientation startAngle, Coord end, Orientation endAngle);

	public void setInterpolator(Interpolator i);

}
