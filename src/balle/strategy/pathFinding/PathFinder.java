package balle.strategy.pathFinding;

import java.util.ArrayList;

import balle.main.drawable.Drawable;
import balle.strategy.pathFinding.path.Path;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Snapshot;

public interface PathFinder {

	public Path[] getPaths(Snapshot s, Coord end, Orientation endAngle);

	public ArrayList<Drawable> getDrawables();

}
