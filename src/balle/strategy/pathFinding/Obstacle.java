package balle.strategy.pathFinding;

import balle.misc.Globals;
import balle.strategy.curve.Curve;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Snapshot;
import balle.world.objects.FieldObject;
import balle.world.objects.Pitch;
import balle.world.objects.Robot;

public abstract class Obstacle {

	public static final double ROBOT_CLEARANCE = 0.1 + Math.sqrt(Math.pow(
			Globals.ROBOT_LENGTH, 2) + Math.pow(Globals.ROBOT_WIDTH, 2));
	

	public static final double WALL_CLEARANCE = Globals.ROBOT_WIDTH / 2;

	// public static final double WALL_CLEARANCE = 0.1 + (Math.sqrt(Math.pow(
	// Globals.ROBOT_LENGTH, 2) + Math.pow(Globals.ROBOT_WIDTH, 2)) / 2);
			
	public static final double BALL_CLEARANCE = 0.05 + Globals.ROBOT_WIDTH
			+ Globals.BALL_RADIUS;

	private FieldObject source;
	protected double clearance, strictClearance;

	public double getClearance(boolean leniency) {
		if (leniency)
			return clearance;
		else
			return strictClearance;
	}

	public FieldObject getSource() {
		return source;
	}

	public Obstacle(FieldObject source, double clearance) {
		this.source = source;
		this.clearance = clearance;
		this.strictClearance = Globals.ROBOT_WIDTH / 2;
	}

	protected FieldObject getFieldObjeect() {
		return source;
	}

	protected boolean isMovingTowards(Curve c) {
		Coord pos = getPosition();
		return c.closestPoint(pos).dist(pos) < pos.dist(c.pos(0));
	}

	public Coord getPosition() {
		return getFieldObjeect().getPosition();
	}

	/**
	 * return true if the Coord is not within the clearance distance
	 * 
	 * @param c
	 * @return
	 */
	public abstract boolean clear(Coord tar, Coord crd, boolean leniency);

	public Coord[][] getWaypoint(Snapshot s, Coord curr, Curve curveSoFar) {
		Coord c = new Coord(0, this.getClearance(true));
		Orientation o = curveSoFar.pos(1).sub(curr).getOrientation();

		return new Coord[][] { new Coord[] { c.rotate(o).add(getPosition()) },
				new Coord[] { c.rotate(o.getOpposite()).add(getPosition()) } };
		
// // if (clear(curr)) { Coord c = new Coord(0, this.getClearance());
		// Orientation oObsEnd = curveSoFar.pos(1).sub(this).getOrientation();
		// Orientation oObsCur = curr.sub(this).getOrientation();
		// Orientation o = oObsEnd;
		// boolean onOppositeSide = oObsEnd.getUnitCoord().dot(
		// oObsCur.getUnitCoord()) < -0.2;
		// if (!onOppositeSide) {
		// o = o.add(new Orientation(Math.PI / 2));
		// }
		// Coord c = new Coord(0, this.getClearance());
		// return new Coord[][] { new Coord[] { c.rotate(o).add(this) },
		// new Coord[] { c.rotate(o.getOpposite()).add(this) } };
		
	}

	@Override
	public String toString() {
		if (source instanceof Robot) {
			return "Opponent";
		} else if (source instanceof Pitch) {
			return "Wall";
		} else {
			return "Something";
		}
	}

}
