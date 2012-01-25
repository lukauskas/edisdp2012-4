package balle.world;

/**
 * Immutable
 */
public class FieldObject {

	private Coord position;
	private double angle;
	private double velocity;
	
	public FieldObject(Coord position, double angle, double velocity) {
		super();
		this.position = position;
		this.angle = angle;
		this.velocity = velocity;
	}
	
	public Coord getPosition() {
		return position;
	}

	public double getAngle() {
		return angle;
	}
	
	public double getVelocity() {
		return velocity;
	}
	
}
