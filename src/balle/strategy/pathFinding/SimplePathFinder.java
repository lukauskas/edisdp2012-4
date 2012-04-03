package balle.strategy.pathFinding;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;

import org.apache.log4j.Logger;

import balle.main.drawable.Dot;
import balle.main.drawable.Drawable;
import balle.strategy.curve.Curve;
import balle.strategy.curve.Interpolator;
import balle.strategy.curve.Spline;
import balle.strategy.pathFinding.path.MaxSpeedPath;
import balle.strategy.pathFinding.path.Path;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Snapshot;
import balle.world.objects.Pitch;
import balle.world.objects.Robot;

public class SimplePathFinder implements PathFinder {

	// Logger
	public static final Logger LOG = Logger.getLogger(SimplePathFinder.class);

	// Instance variables.
	protected Interpolator interpolator;

	// Temporary
	protected Coord start, end;
	protected Orientation startAngle, endAngle;
	protected Spline currentBest;

	public SimplePathFinder(Interpolator i) {
		this.interpolator = i;
	}

	@Override
	public Path[] getPaths(Snapshot s, Coord end, Orientation endAngle)
			throws ValidPathNotFoundException {
		
		Robot robot = s.getBalle();
		Coord start = robot.getPosition();
		Orientation startAngle = robot.getOrientation();

		return getPaths(s, start, startAngle, end, endAngle);
	}

	protected Path[] getPaths(Snapshot s, Coord start,
 Orientation startAngle,
			Coord end, Orientation endAngle) throws ValidPathNotFoundException {
		
		drawables = new ArrayList<Drawable>();
		// Initialise temporary variables.
		this.start = start;
		this.startAngle = startAngle;
		this.end = end;
		this.endAngle = endAngle;

		Spline spline = getPath(start, end, s);
		Stack<Coord> list = spline.getComponents();

		// remove duplicates (in sequence)
		Coord last = list.get(0);
		for (int i = 1; i < list.size(); i++) {
			if (last.equals(list.get(i))) {
				list.remove(i);
				i--;
			}
			last = list.get(i);
		}

		// Convert to a path.
		return new Path[] { new MaxSpeedPath(getCurve(list)) };
	}

	/**
	 * Given the start/end waypoints for a path, modify it by avoiding
	 * obstacles.
	 * 
	 * @param pathStart
	 *            Coord of the start of the path
	 * @param pathEnd
	 *            Coord of the end of the path
	 * @param s
	 *            current snapshot
	 * @return new waypoints avoiding obstacles (includes pathStart and pathEnd)
	 * @throws ValidPathNotFoundException
	 */
	public Spline getPath(Coord pathStart, Coord pathEnd, Snapshot s)
			throws ValidPathNotFoundException {
		currentBest = null;
		Stack<Coord> startPath = new Stack<Coord>();
		startPath.add(pathStart);
		return getPath(startPath, pathEnd, s, 0);
	}

	/**
	 * GET PATH.
	 * 
	 * @param pathSoFar
	 *            Preceding path to this decision. [ This is necessary for
	 *            correct collision detection ]
	 * 
	 * @param pathEnd
	 *            Desired end-point of path.
	 * 
	 * @param s
	 *            Current Snapshot.
	 * 
	 * @param currentDepth
	 *            How many recursions have happened already.
	 * 
	 * @return Best possible route from here onwards, null if no routes are
	 *         valid.
	 * 
	 * @throws ValidPathNotFoundException
	 *             When there are no possible routes.
	 */
	private Spline getPath(Stack<Coord> pathSoFar, Coord pathEnd, Snapshot s,
			int currentDepth) throws ValidPathNotFoundException {

		// Get current curve.
		pathSoFar.push(pathEnd);
		Spline currentCurve = getCurve(pathSoFar);
		drawables.add(currentCurve);
		pathSoFar.pop();

		// If path has already collided with an obstacle
		if (!isClearSoFar(currentCurve, s, pathSoFar.size()))
			throw new ValidPathNotFoundException(
					"Already collided with obstacle.");

		// Find obstacles, if any.
		ArrayList<Obstacle> obsticle = isClear(currentCurve, s);

		if (obsticle.size() == 0) {

			// END POINT! ______
			return currentCurve;
			// _________________

		} else if (currentDepth > 4) {
			throw new ValidPathNotFoundException("Current depth exceeds limit.");

		} else {

			// Get new way-point(s) possibilities.
			ArrayList<Coord[]> possibleNextWaypoints = new ArrayList<Coord[]>();

			for (Obstacle each : obsticle)
				for (Coord[] pnw : each.getWaypoint(s, pathSoFar.peek(),
						currentCurve))
					possibleNextWaypoints.add(pnw);

			// add all options to the possible paths list
			ArrayList<Spline> possiblePaths = new ArrayList<Spline>();
			ArrayList<ValidPathNotFoundException> failures = new ArrayList<ValidPathNotFoundException>();
			for (Coord[] waypoints : possibleNextWaypoints) {

				// Recreate pathSoFar and add all way-points ready for next
				// iteration.
				@SuppressWarnings("unchecked")
				Stack<Coord> newPath = (Stack<Coord>) pathSoFar.clone();
				for (Coord wp : waypoints)
					newPath.push(wp);

				try {

					// RECURSIVE CALL!_____________________________
					// Depth first search, COMPLETE path comes out.
					Spline validCurve = getPath(newPath, pathEnd, s,
							currentDepth + 1);
					// _____________________________________________

					// Add valid curve to hopefuls.
					possiblePaths.add(validCurve);

					// Drawables
					for (Coord each : waypoints)
						drawables.add(new Dot(each, Color.CYAN));

				} catch (ValidPathNotFoundException e) {
					// Ignore path, hope others fill gap
					// otherwise exception will be thrown again later
					// [ :S fingers crossed ]
					failures.add(e);
				}
			}

			// These should be complete, going to the end.
			Spline contender = best(possiblePaths);

			// Debugging information.
			if (contender == null) {
				throw new ValidPathNotFoundException("This many failures "
						+ failures.size()
						+ ", and this many possible solutions "
						+ possiblePaths.size() + ".");
			}

			if (currentBest == null
					|| currentBest.length() > contender.length()) {

				// Update currentBest.
				currentBest = contender;
				return contender;
				
			} else {
				// Prune this branch.
				throw new ValidPathNotFoundException(
						"Current branch is being pruned.");
			}
		}
	}

	protected boolean isClearSoFar(Spline c, Snapshot s, int len) {
		return (len <= 1) || (isClear(c.getSubSpline(0, len), s) == null);
	}

	protected ArrayList<Obstacle> isClear(Curve c, Snapshot s) {
		ArrayList<Obstacle> out = new ArrayList<Obstacle>();

		// TODO: David says "Review"
		// James says "Yeah, this changes
		// behaviour with more obstacles"
		if (s.getOpponent().getPosition() == null)
			return out;

		Coord pos = s.getBalle().getPosition();
		for (Obstacle o : getObstacles(s)) {
			Obstacle adding = null;

			// if we are inside the clearance area, just assume the obstical is
			// in the way
			// This is not applicable if the end point is also within the
			// clearance area
			// this is not really an obstacle if our path is already moving away
			if (!o.clear(start) && o.isMovingTowards(c)) {
				adding = o;
			}

			// (o.getSource() instanceof Pitch)

			// check that the current path does not go from out of
			// the object clearance to into the clearance
			if (adding == null) {
				boolean in = !o.clear(pos);
				for (double i = 0; i < 1; i += 0.01) {
					boolean newIn = !o.clear(c.pos(i));
					if (!in && newIn) {
						adding = o;
						break;
					}
				}
			}

			if (adding != null)
				out.add(adding);
		}
		return out;
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

	protected Spline getCurve(Vector<Coord> path) {
		// Convert to array.
		Coord[] out = new Coord[path.size()];
		for (int i = 0; i < out.length; i++)
			out[i] = path.get(i);
		return interpolator.getCurve(out, startAngle, endAngle);
	}

	/**
	 * CHOOSE BEST PATH OUT OF CURRENT SELECTION.
	 * 
	 * @param hopefulls
	 *            Path possibilities
	 * @return Best path out of this selection.
	 * 
	 */
	protected Spline best(ArrayList<Spline> hopefulls) {
		Spline currBest = null;
		for (Spline each : hopefulls)
			if (currBest == null || currBest.length() > each.length())
				currBest = each;
		return currBest;
	}

	// Visual Output \\

	ArrayList<Drawable> drawables = new ArrayList<Drawable>();

	public ArrayList<Drawable> getDrawables() {
		return drawables;
	}

	// Collisions

	public Obstacle[] getObstacles(Snapshot s) {

		return new Obstacle[] {

				new Obstacle(s.getOpponent(), Math.min(
						Obstacle.ROBOT_CLEARANCE, s.getOpponent().getPosition()
								.dist(end))) {
					@Override
					public boolean clear(Coord c) {
						return c.dist(getPosition()) > clearance;
					}
				},

				new Obstacle(s.getPitch(), Obstacle.WALL_CLEARANCE) {
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
						double x = c.getX(), y = c.getY(),

						xmin = p.getMinX() + getClearance(), xmax = p.getMaxX()
								- getClearance(), ymin = p.getMinY()
								+ getClearance(), ymax = p.getMaxY()
								- getClearance();

						return new Coord(Math.min(xmax, Math.max(xmin, x)),
								Math.min(ymax, Math.max(ymin, y)));
					}

					@Override
					public Coord[][] getWaypoint(Snapshot s, Coord curr, Curve c) {

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
	}

}
