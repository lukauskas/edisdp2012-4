package balle.world;

/**
 * Immutable
 */
public class FieldObject {

    private final Coord  position;
    private final double angle;
    private final double velocity;

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
