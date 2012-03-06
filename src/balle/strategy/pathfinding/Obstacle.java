package balle.strategy.pathfinding;

import balle.misc.Globals;
import balle.world.Coord;
import balle.world.objects.FieldObject;

public class Obstacle extends Coord {

	public static final double ROBOT_CLEARANCE = 0.1 + Math.sqrt(Math.pow(
			Globals.ROBOT_LENGTH, 2) + Math.pow(Globals.ROBOT_WIDTH, 2));
	public static final double BALL_CLEARANCE = 0.05 + Globals.ROBOT_WIDTH
			+ Globals.BALL_RADIUS;

	private double clearance;

	private FieldObject source;

	protected double getClearance() {
		return clearance;
	}

	protected FieldObject getSource() {
		return source;
	}

	public Obstacle(FieldObject source, double clearance) {
		super(source.getPosition().getX(), source.getPosition().getY());
		this.source = source;
		this.clearance = clearance;
	}

	public Obstacle(Coord pos, double clearance) {
		super(pos.getX(), pos.getY());
		this.clearance = clearance;
	}

	// public Obstacle(double x, double y, double clearance) {
	// super(x, y);
	// this.clearance = clearance;
	// }

	/**
	 * return true if the Coord is not within the clearance distance
	 * 
	 * @param c
	 * @return
	 */
	public boolean clear(Coord c) {
		return c.dist(this) > clearance;
	}

}
