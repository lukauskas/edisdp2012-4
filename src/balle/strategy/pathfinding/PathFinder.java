package balle.strategy.pathfinding;

import java.util.ArrayList;

import balle.main.drawable.Drawable;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Snapshot;

public interface PathFinder {

	public AbstractPath[] getPaths(Snapshot s, Coord end, Orientation endAngle);

	public ArrayList<Drawable> getDrawables();

}
