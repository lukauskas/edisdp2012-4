package balle.world;

public class Velocity extends Coord {

    protected final double timeDelta;

    public Velocity(double x, double y, double timeDelta, boolean estimated) {
        super(x / timeDelta, y / timeDelta, estimated);
        this.timeDelta = timeDelta;
    }

    public Velocity(double x, double y, double timeDelta) {
        super(x / timeDelta, y / timeDelta);
        this.timeDelta = timeDelta;
    }

    public Velocity(Coord coord, double timeDelta) {
        this(coord.getX() / timeDelta, coord.getY() / timeDelta, timeDelta,
                coord.isEstimated());
    }

    public Velocity adjustLength(double newTimeDelta) {
        return new Velocity(this.mult(newTimeDelta / timeDelta), newTimeDelta);
    }
}
