package balle.strategy.pathfinding;

import balle.misc.Globals;
import balle.strategy.curve.Curve;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Snapshot;
import balle.world.objects.StaticFieldObject;

public abstract class Obstacle extends Coord {

	public static final double ROBOT_CLEARANCE = 0.1 + Math.sqrt(Math.pow(
			Globals.ROBOT_LENGTH, 2) + Math.pow(Globals.ROBOT_WIDTH, 2));
	public static final double WALL_CLEARANCE = 0.1 + (Math.sqrt(Math.pow(
			Globals.ROBOT_LENGTH, 2) + Math.pow(Globals.ROBOT_WIDTH, 2)) / 2);
	public static final double BALL_CLEARANCE = 0.05 + Globals.ROBOT_WIDTH
			+ Globals.BALL_RADIUS;

	protected double clearance;

	private StaticFieldObject source;

	protected double getClearance() {
		return clearance;
	}

	protected StaticFieldObject getSource() {
		return source;
	}

	public Obstacle(StaticFieldObject source, double clearance) {
		super(source.getPosition().getX(), source.getPosition().getY());
		this.source = source;
		this.clearance = clearance;
	}

	protected boolean isMovingTowards(Curve c) {
		return c.closestPoint(this).dist(this) < this.dist(c.pos(0));
	}

	/**
	 * return true if the Coord is not within the clearance distance
	 * 
	 * @param c
	 * @return
	 */
	public abstract boolean clear(Coord c);

	public Coord getWaypoint(Snapshot s, Orientation o, Curve curveSoFar) {
		Coord c = new Coord(0, this.getClearance());
		c = c.rotate(o).add(this);

		return c;
	}

}