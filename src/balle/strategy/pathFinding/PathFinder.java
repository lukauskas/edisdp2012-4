package balle.strategy.pathFinding;

import java.util.ArrayList;
import java.util.List;

import balle.main.drawable.Drawable;
import balle.strategy.pathFinding.path.Path;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Snapshot;

public interface PathFinder {

	public List<Path> getPaths(Snapshot s, Coord end, Orientation endAngle)
			throws ValidPathNotFoundException;

	public ArrayList<Drawable> getDrawables();

}
