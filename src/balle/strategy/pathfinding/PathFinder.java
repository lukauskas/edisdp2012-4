package balle.strategy.pathfinding;

import java.util.ArrayList;

import balle.main.drawable.Drawable;
import balle.strategy.curve.Curve;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Snapshot;

public interface PathFinder {

	public Curve getPath(Snapshot s, Coord start, Orientation startAngle,
			Coord end, Orientation endAngle);

	public ArrayList<Drawable> getDrawables();

}
