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
		list.add(s.getBalle().getPosition());
		list = getPath(list, s);

		// Convert to a curve.
		return getCurve(list);
	}

	@SuppressWarnings("unchecked")
	public Stack<Coord> getPath(Stack<Coord> path, Snapshot s) {

		// Make new curve.
		path.add(end);

		// Check for intersections.
		Obstacle obsticle = isClear(getCurve(path), s);
		if (obsticle == null || path.size() > 4) {
			return path;
		} else {		
			path.pop();

			Coord[][] waypoints = obsticle.getWaypoint(s, null, getCurve(path));
			for (Coord[] eachlist : waypoints)
				for (Coord each : eachlist)
					drawables.add(new Dot(each, Color.CYAN));

			ArrayList<Stack<Coord>> finalpath = new ArrayList<Stack<Coord>>();
			for (Coord[] waypoint : waypoints) {

				Stack<Coord> currPath = (Stack<Coord>) path.clone();
				for (Coord each : waypoint)
					currPath.add(each);

				finalpath.add(getPath(currPath, s));
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

					@Override
					public Coord[][] getWaypoint(Snapshot s, Orientation o,
							Curve c) {
						Coord prev = c.pos(0), p1, p2, end = c.pos(1);
						double t1, t2;
						Pitch pitch = s.getPitch();

						// if starting in clearance area, take shortest path out
						if (!clear(prev)) {
							return new Coord[][] { new Coord[] { new Coord(
									Math.min(
									pitch.getMaxX(),
									Math.max(pitch.getMinX(), prev.getX())),
									Math.min(
											pitch.getMaxY(),
											Math.max(pitch.getMinY(),
													prev.getY()))) } };

						} else {
							// find enter and exit points of the clearance area
							boolean firstChange = false;
							for (t1 = 0.01; t1 <= 1; t1 += 0.01) {
								p1 = c.pos(t1);
								if (!clear(p1)) {
									break;
								}
							}
							for (t2 = 0.01; t2 <= 1; t2 += 0.01) {
								p2 = c.pos(t2);
								if (clear(p2)) {
									break;
								}
							}
						}
						// may not have an exit point
						boolean hasSecondPoint = t2 != 1;
						int numWayPoints = (hasSecondPoint) ? 2 : 1;
						Coord[] wayPoints = new Coord[2];
						if (hasSecondPoint) {
							wayPoints[0] = p1;
							wayPoints[1] = p2;
						} else {
							wayPoints[0] = p1;
							System.out.println("Implementation needed!");
							boolean horiz = p1.getX() < pitch.getMaxX()
									&& p1.getX() > pitch.getMinX();
							if (horiz) {

							} else {

							}

						}

						return new Coord[][] { wayPoints };
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
					|| getCurve(currBest).length() < getCurve(each).length())
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
