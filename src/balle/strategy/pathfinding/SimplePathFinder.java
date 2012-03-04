package balle.strategy.pathfinding;

import java.util.Stack;
import java.util.Vector;

import balle.strategy.curve.Curve;
import balle.strategy.curve.Interpolator;
import balle.world.Coord;
import balle.world.Line;
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

		Stack<Coord> list = new Stack<Coord>();

		list.add(s.getBalle().getPosition());
		list = getWaypoint(s.getBalle().getPosition(), list, s);

		// Convert to a curve.
		return getCurve(list);
	}

	@SuppressWarnings("unchecked")
	public Stack<Coord> getWaypoint(Coord pos, Stack<Coord> path,
			Snapshot s) {
		
		// Make new curve.
		path = (Stack<Coord>) path.clone();
		path.add(end);
		Line c = new Line(pos, end);

		// Check for intersections.
		Robot obsticle = isClear(c, s);
		if (obsticle == null) {
			return path;
		} else {
			path.pop();
			
			Orientation toObsticle = obsticle.getPosition().sub(path.peek()).getOrientation();

			double birth = 0.3;
			Coord left, right;
			left = new Coord(birth, 0).rotate(toObsticle);
			right = new Coord(-birth, 0).rotate(toObsticle);

			Stack<Coord> leftPath, rightPath;
			leftPath = getWaypoint(left, path, s);
			rightPath = getWaypoint(right, path, s);

			if (leftPath.size() < rightPath.size())
				return leftPath;
			else
				return rightPath;
		}
	}

	protected Robot isClear(Line curve, Snapshot s) {
		Robot r = s.getOpponent();
		Coord rr = r.getPosition();

		double birth = 0.3;
		Line other = new Line(new Coord(birth, 0).rotate(r.getOrientation())
				.add(rr), new Coord(-birth, 0).rotate(r.getOrientation()).add(
				rr));
		if (other.intersects(curve))
			return r;

		return null;
	}

	protected Curve getCurve(Vector<Coord> path) {
		// Convert to array.
		Coord[] out = new Coord[path.size()];
		for (int i = 0; i < out.length; i++)
			out[i] = path.get(i);
		return interpolator.getCurve(out, startAngle, endAngle);
	}

}
