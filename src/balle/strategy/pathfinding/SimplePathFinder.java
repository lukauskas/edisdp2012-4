package balle.strategy.pathfinding;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;

import balle.main.drawable.Dot;
import balle.main.drawable.Drawable;
import balle.strategy.curve.Curve;
import balle.strategy.curve.Interpolator;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Snapshot;
import balle.world.objects.Pitch;

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

		Stack<Coord> list = new Stack<Coord>();
		list.push(start);
		list = getPath(list, s);

		// remove duplicates (in sequence)
		Coord last = list.get(0);
		for (int i = 1; i < list.size(); i++) {
			if (last.equals(list.get(i))) {
				list.remove(i);
				i--;
			}
			last = list.get(i);
		}

		// Convert to a curve.
		return getCurve(list);
	}

	@SuppressWarnings("unchecked")
	public Stack<Coord> getPath(Stack<Coord> path, Snapshot s) {
		Coord curr = path.peek();

		// Make new curve.
		path.add(end);

		// Check for intersections.
		Obstacle obsticle = isClear(getCurve(path), s);
		if (obsticle == null || path.size() > 4) {
			return path;
		} else {

			Coord[][] waypoints = obsticle.getWaypoint(s, curr, getCurve(path));

			for (Coord[] eachlist : waypoints)
				for (Coord each : eachlist)
					drawables.add(new Dot(each, Color.CYAN));

			path.pop();

			ArrayList<Stack<Coord>> finalpath = new ArrayList<Stack<Coord>>();
			for (Coord[] waypoint : waypoints) {

				Stack<Coord> currPath = (Stack<Coord>) path.clone();
				for (Coord each : waypoint)
					currPath.push(each);

				finalpath.add(getPath(currPath, s));
				// drawables.add(getCurve(currPath));
			}

			// if (leftPath.size() < rightPath.size())

			return best(finalpath);
		}
	}

	protected Obstacle isClear(Curve c, Snapshot s) {
		Obstacle[] obstacles = new Obstacle[] {

				new Obstacle(s.getOpponent(), Math.min(
						Obstacle.ROBOT_CLEARANCE, s.getOpponent().getPosition()
								.dist(end))) {
					@Override
					public boolean clear(Coord c) {
						return c.dist(this) > clearance;
					}
				}, new Obstacle(s.getPitch(), Obstacle.WALL_CLEARANCE) {

					@Override
					public boolean clear(Coord c) {
						Pitch p = (Pitch) getSource();
						if (c.getX() < p.getMinX() + clearance)
							return false;
						if (c.getX() > p.getMaxX() - clearance)
							return false;
						if (c.getY() < p.getMinY() + clearance)
							return false;
						if (c.getY() > p.getMaxY() - clearance)
							return false;
						return true;
					}

					private double distance(Coord c) {
						double d, x = c.getX(), y = c.getY();

						Pitch p = (Pitch) getSource();
						d = Math.abs(p.getMinX() - x);
						d = Math.min(d, Math.abs(p.getMaxX() - x));
						d = Math.min(d, Math.abs(p.getMaxY() - y));
						d = Math.min(d, Math.abs(p.getMinY() - y));
						return d;
					}

					@Override
					protected boolean isMovingTowards(Curve c) {
						double initD = distance(c.pos(0));
						for (double i = 0.01; i <= 1; i += 0.01)
							if (distance(c.pos(i)) < initD)
								return true;
						return false;
					}
					
					public Coord constrain(Coord c, Pitch p) {
						double 	x = c.getX(),
								y = c.getY();
						return new Coord(
								Math.min(p.getMaxX(), Math.max(p.getMinX(), x)),
 Math.min(
								p.getMaxY(), Math.max(p.getMinY(), y))
								);
					}

					@Override
					public Coord[][] getWaypoint(Snapshot s, Coord curr,
							Curve c) {

						Pitch pitch = (Pitch) getSource();

						ArrayList<Coord> waypoints = new ArrayList<Coord>();
						for (double t = 0; t <= 1; t += 0.01) {
							Coord point = c.pos(t);
							Coord cons = constrain(point, pitch);
							if (!point.equals(cons)) {
								waypoints.add(cons);
							}
						}
						
						if (waypoints.size() == 0) {
							System.out
									.println("aaaaaaaaaaaaaaaaaaaaaaaaaaa34dss7");
							return new Coord[0][0];
						}
						if (waypoints.size() == 1) {
							System.out
									.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbw45732");
							return new Coord[][] { new Coord[] { waypoints
									.get(0) } };
						}
						Coord[] wpa = new Coord[] { waypoints.get(0),
								waypoints.get(waypoints.size() - 1) };
						return new Coord[][] { wpa };
					}
				}
		// new Obstical(s.getBall().getPosition(),
		// Obstical.ROBOT_CLEARANCE)
		};

		Coord pos = s.getBalle().getPosition();
		for (Obstacle o : obstacles) {

			// if we are inside the clearance area, just assume the obstical is
			// in the way
			// This is not applicable if the end point is also within the
			// clearance area
			// this is not really an obstacle if our path is already moving away
			if (!o.clear(start) && o.isMovingTowards(c)) {
				return o;
			}

			// (o.getSource() instanceof Pitch)

			// check that the current path does not go from out of
			// the object clearance to into the clearance
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

	//
	// protected Coord getWaypoint(Snapshot s, Obstacle obs, Orientation o) {
	// Line l, out, walls[];
	// walls = s.getPitch().getWalls();
	// l = new Line(obs, obs.add(new Coord(0, 10).rotate(o)));
	//
	// Coord wallIntersect = null;
	// for (Line wall : walls) {
	// wallIntersect = l.getIntersect(wall);
	// if (wallIntersect != null)
	// break;
	// }
	//
	// out = new Line(wallIntersect, obs);
	// drawables.add(new DrawableLine(out, Color.RED));
	//
	// Coord c = new Coord(0, obs.getClearance());
	// c = c.rotate(o).add(obs);
	//
	// return c;
	// }

	protected Curve getCurve(Vector<Coord> path) {
		// Convert to array.
		Coord[] out = new Coord[path.size()];
		for (int i = 0; i < out.length; i++)
			out[i] = path.get(i);
		return interpolator.getCurve(out, startAngle, endAngle);
	}

	protected Stack<Coord> best(ArrayList<Stack<Coord>> hopefulls) {
		Stack<Coord> currBest = null;
		for (Stack<Coord> each : hopefulls) {

			if (currBest == null
					|| getCurve(currBest).length() > getCurve(each).length())
				currBest = each;
		}
		return currBest;

	}

	// Visual Output \\

	ArrayList<Drawable> drawables = new ArrayList<Drawable>();

	public ArrayList<Drawable> getDrawables() {
		return drawables;
	}

}
