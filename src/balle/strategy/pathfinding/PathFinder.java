package balle.strategy.pathfinding;

import balle.strategy.curve.Curve;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Snapshot;

public interface PathFinder {

	public Curve getPath(Snapshot s, Coord start, Orientation startAngle,
			Coord end, Orientation endAngle);

}
