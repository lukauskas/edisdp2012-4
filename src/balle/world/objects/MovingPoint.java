package balle.world.objects;

import balle.world.Coord;
import balle.world.Velocity;

/**
 * Immutable
 */
public class MovingPoint {

    private final Coord    position;
    private final Velocity velocity;

    public MovingPoint(Coord position, Velocity velocity) {
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