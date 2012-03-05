package balle.strategy.pathfinding;

import balle.misc.Globals;
import balle.world.Coord;

public class Obstical extends Coord {

	public static final double ROBOT_CLEARANCE = 0.1 + Math.sqrt(Math.pow(
			Globals.ROBOT_HEIGHT, 2) + Math.pow(Globals.ROBOT_WIDTH, 2));
	public static final double BALL_CLEARANCE = 0.05 + Globals.ROBOT_WIDTH
			+ Globals.BALL_RADIUS;

	private double clearance;

	protected double getClearance() {
		return clearance;
	}

	public Obstical(Coord pos, double clearance) {
		super(pos.getX(), pos.getY());
		this.clearance = clearance;
	}

	public Obstical(double x, double y, double clearance) {
		super(x, y);
		this.clearance = clearance;
	}

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
