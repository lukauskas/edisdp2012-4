package balle.world;

import org.jbox2d.common.Vec2;

public class Velocity extends Coord {

    protected final double timeDelta;

	public Velocity(double x, double y, double timeDelta, int estimatedFrames) {
		super(x / timeDelta, y / timeDelta, estimatedFrames);
        this.timeDelta = timeDelta;
    }

    public Velocity(double x, double y, double timeDelta) {
        super(x / timeDelta, y / timeDelta);
        this.timeDelta = timeDelta;
    }

    public Velocity(Coord coord, double timeDelta) {
        this(coord.getX() / timeDelta, coord.getY() / timeDelta, timeDelta,
				coord.getEstimatedFrames());
    }

    public Velocity adjustLength(double newTimeDelta) {
        return new Velocity(this.mult(newTimeDelta / timeDelta), newTimeDelta);
    }

	/**
	 * @return A vec2 representing this velocity, scaled to the jbox2d units.
	 */
	public Vec2 vec2() {
		return new Vec2((float) (getX() / timeDelta),
				(float) (getY() / timeDelta));
	}

}
