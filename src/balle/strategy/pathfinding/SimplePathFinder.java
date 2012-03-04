package balle.strategy.pathfinding;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;

import balle.main.drawable.Dot;
import balle.main.drawable.Drawable;
import balle.main.drawable.DrawableLine;
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
		drawables = new ArrayList<Drawable>();

		// Initialise temporary variables.
		this.start = start;
		this.startAngle = startAngle;
		this.end = end;
		this.endAngle = endAngle;

		Stack<Coord> list = getWaypoint(s.getBalle().getPosition(),
				new Stack<Coord>(), s);

		// Convert to a curve.
		return getCurve(list);
	}

	@SuppressWarnings("unchecked")
	public Stack<Coord> getWaypoint(Coord pos, Stack<Coord> path,
			Snapshot s) {
		
		// Make new curve.
		path = (Stack<Coord>) path.clone();
		path.add(pos);
		path.add(end);
		Line c = new Line(pos, end);

		// Check for intersections.
		Robot obsticle = isClear(c, s);
		if (obsticle == null || path.size() > 4) {
			return path;
		} else {
			path.pop();
			
			Orientation toObsticle = obsticle.getPosition().sub(path.peek()).getOrientation();

			double birth = 0.3;
			Coord left, right;
			left = new Coord(0.1, birth).rotate(toObsticle).add(
					obsticle.getPosition());
			right = new Coord(0.1, -birth).rotate(toObsticle).add(
					obsticle.getPosition());

			drawables.add(new Dot(left, Color.CYAN));
			drawables.add(new Dot(right, Color.CYAN));

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

		// Make line
		double birth = 0.2;
		Line other = new Line(new Coord(0, birth), new Coord(0, -birth));
		other = other.rotate(curve.getA().sub(curve.getB()).getOrientation());
		other = other.add(r.getPosition());
		
		System.out.println(curve + "\t" + r.getPosition() + "\t" + other + "\t"
				+ other.intersects(curve));

		if (other.intersects(curve))
			return r;

		drawables.add(new DrawableLine(other, Color.BLUE));
		drawables.add(new DrawableLine(curve, Color.BLUE));

		return null;
	}

	protected Curve getCurve(Vector<Coord> path) {
		// Convert to array.
		Coord[] out = new Coord[path.size()];
		for (int i = 0; i < out.length; i++)
			out[i] = path.get(i);
		return interpolator.getCurve(out, startAngle, endAngle);
	}

	ArrayList<Drawable> drawables = new ArrayList<Drawable>();
	public ArrayList<Drawable> getDrawables() {
		return drawables;
	}

}
