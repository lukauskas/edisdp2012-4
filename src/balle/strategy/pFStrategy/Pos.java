package balle.strategy.pFStrategy;

import balle.world.Coord;

//A robot Pos, includes location and angle.
public class Pos {
	private final Point location;
	private final double angle;

	public Pos(Point point, double angle) {
		this.location = point;
		this.angle = angle;
	}

	public double getAngle() {
		return angle;
	}

	public Point getLocation() {
		return location;
	}

	@Override
	public String toString() {
		return location.toString() + "," + angle;

	}
}
