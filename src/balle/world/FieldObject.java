package balle.world;

/**
 * Immutable
 */
public class FieldObject {

    private final Coord    position;
    private final Velocity velocity;

    public FieldObject(Coord position, Velocity velocity) {
        super();

        this.position = position;
        this.velocity = velocity;
    }

    public Coord getPosition() {
        return position;
    }

    public Velocity getVelocity() {
        return velocity;
    }

}
