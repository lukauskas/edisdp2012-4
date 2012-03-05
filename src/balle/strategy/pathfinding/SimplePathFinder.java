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

		Stack<Coord> list = getPath(s.getBalle().getPosition(),
				new Stack<Coord>(), s);

		// Convert to a curve.
		return getCurve(list);
	}

	@SuppressWarnings("unchecked")
	public Stack<Coord> getPath(Coord pos, Stack<Coord> path,
			Snapshot s) {
		
		// Make new curve.
		path = (Stack<Coord>) path.clone();
		path.add(pos);
		path.add(end);

		// Check for intersections.
		Obstical obsticle = isClear(getCurve(path), s);
		if (obsticle == null || path.size() > 4) {
			return path;
		} else {
			path.pop();
			
			Orientation toObsticle = obsticle.sub(path.peek()).getOrientation();

			Coord left, right;
			left = getWaypoint(s, obsticle, pos, toObsticle);
			right = getWaypoint(s, obsticle, pos,
					toObsticle.getOpposite());

			drawables.add(new Dot(left, Color.CYAN));
			drawables.add(new Dot(right, Color.CYAN));

			Stack<Coord> leftPath, rightPath;
			leftPath = getPath(left, path, s);
			rightPath = getPath(right, path, s);

			if (leftPath.size() < rightPath.size())
				return leftPath;
			else
				return rightPath;
		}
	}

	protected Obstical isClear(Curve c, Snapshot s) {
		Obstical [] obsticals = new Obstical[]{
				
			new Obstical(s.getOpponent().getPosition(),
					Obstical.ROBOT_CLEARANCE),
		// new Obstical(s.getBall().getPosition(),
		// Obstical.ROBOT_CLEARANCE)
		};
		
		Coord pos = s.getBalle().getPosition();
		for(Obstical o : obsticals) {
			// check that the current path does not go from out of
			// the object clearance to into the clearance
			// TODO make sure that if the robot is already in the clearance
			// area,#
			// that it doesn't move through the obstical
			boolean in = !o.clear(pos);
			for (double i = 0; i < 1; i += 0.01) {
				boolean newIn = !o.clear(c.pos(i));
				if (!in && newIn) {
					return o;
				}
			}
		}
		return null;
	}

	protected Coord getWaypoint(Snapshot s, Coord obs, Coord pos, Orientation o) {
		Line l, out, walls[];
		walls = s.getPitch().getWalls();
		l = new Line(obs, obs.add(new Coord(0, 10).rotate(o)));

		Coord wallIntersect = null;
		for (Line wall : walls) {
			wallIntersect = l.getIntersect(wall);
			if (wallIntersect != null)
				break;
		}

		out = new Line(wallIntersect, obs);
		drawables.add(new DrawableLine(out, Color.RED));
		return out.midpoint();
	}

	protected Curve getCurve(Vector<Coord> path) {
		// Convert to array.
		Coord[] out = new Coord[path.size()];
		for (int i = 0; i < out.length; i++)
			out[i] = path.get(i);
		return interpolator.getCurve(out, startAngle, endAngle);
	}

	// Visual Output \\

	ArrayList<Drawable> drawables = new ArrayList<Drawable>();
	public ArrayList<Drawable> getDrawables() {
		return drawables;
	}

}
