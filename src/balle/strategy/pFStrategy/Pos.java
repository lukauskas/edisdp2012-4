package main.strategy.pFStrategy;

//A robot Pos, includes location and angle.
public class Pos {
	private final Point location;
	private final double angle;

	public Pos(Point loc, double angle) {
		this.location = loc;
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
