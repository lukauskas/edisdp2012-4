package balle.strategy.pathfinding;

import java.util.ArrayList;

import balle.strategy.curve.Curve;
import balle.strategy.curve.Interpolator;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Snapshot;
import balle.world.objects.Robot;

public class SimplePathFinder implements PathFinder {

	// Instance variables.
	protected Interpolator interpolator;

	// Temporary
	protected Coord start, end;
	protected Orientation startAngle, endAngle;

	public SimplePathFinder(Interpolator i) {
		this.interpolator = i;
	}

	@Override
	public Curve getPath(Snapshot s, Coord start, Orientation startAngle,
			Coord end, Orientation endAngle) {

		// Initialise temporary variables.
		this.start = start;
		this.startAngle = startAngle;
		this.end = end;
		this.endAngle = endAngle;

		ArrayList<Coord> list = new ArrayList<Coord>();

		list.add(s.getBalle().getPosition());
		list = getWaypoint(s.getBalle().getPosition(), list, s);

		// Convert to a curve.
		return getCurve(list);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Coord> getWaypoint(Coord pos, ArrayList<Coord> path,
			Snapshot s) {
		
		// Make new curve.
		path = (ArrayList<Coord>) path.clone();
		path.add(end);
		Curve c = getCurve(path);

		// Check for intersections.
		Robot obsticle = isClear(c, s);
		if (obsticle == null) {
			return path;
		} else {

			// TODO check the branches

			return null;
		}
	}

	protected Robot isClear(Curve curve, Snapshot s) {
		
		// TODO check for collisions
		
		return null;
	}

	protected Curve getCurve(ArrayList<Coord> path) {
		// Convert to array.
		Coord[] out = new Coord[path.size()];
		for (int i = 0; i < out.length; i++)
			out[i] = path.get(i);
		return interpolator.getCurve(out, startAngle, endAngle);
	}

}
