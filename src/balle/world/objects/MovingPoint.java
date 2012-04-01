package balle.world.objects;

import balle.world.Coord;
import balle.world.Velocity;

/**
 * Immutable
 */
public class MovingPoint {

    private final Coord    position;
	private Velocity velocity;

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

	public void setVelocity(Velocity velocity) {
		this.velocity = velocity;
	}

}
