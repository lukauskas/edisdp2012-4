package balle.world;

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
}
