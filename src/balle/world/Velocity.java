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
		this(coord.getX(), coord.getY(), timeDelta,
				coord.getEstimatedFrames());
    }

    /**
     * Adjust length of velocity vector to incorporate new timedelta
     * information.
     * 
     * Note that this is not suitable to change the units of vector as:
     * answer.abs() == this.abs()
     * 
     * @param newTimeDelta
     *            the new time delta
     * @return the velocity
     */
    public Velocity adjustLength(double newTimeDelta) {
        // Get the distance it would have travelled at this velocity during
        // newTimeDelta
        double x = getX() * newTimeDelta;
        double y = getY() * newTimeDelta;

        Velocity ans = new Velocity(x, y, newTimeDelta, getEstimatedFrames());

        return ans;
    }

    /**
     * @return A vec2 representing this velocity, scaled to the jbox2d units.
     */
    public Vec2 vec2(float SCALE) {
        Velocity ans = adjustLength(1000).mult(SCALE * 1000);

        return new Vec2((float) (ans.getX()), (float) (ans.getY()));
	}

    public static Velocity fromVec2(Vec2 velocity, float SCALE) {
        Vec2 ans = velocity.mul(1 / (SCALE));
        return new Velocity(ans.x, ans.y, 1000, 0);
    }

    public Velocity mult(double scalar) {
        Velocity ans = new Velocity(getX() * scalar * timeDelta, getY()
                * scalar
                * timeDelta, timeDelta,
                getEstimatedFrames());

        return ans;
    }

    public Velocity div(double scalar) {
        return mult(1 / scalar);
    }

    public Velocity add(Velocity v) {
        Velocity adjustedV = v.adjustLength(getTimeDelta());
        double x1 = getX() * getTimeDelta();
        double y1 = getY() * getTimeDelta();
        double x2 = adjustedV.getX() * getTimeDelta();
        double y2 = adjustedV.getY() * getTimeDelta();
        
        return new Velocity(x1 + x2, y1 + y2, getTimeDelta(),
                getEstimatedFrames());
    }

    public double getTimeDelta() {
        return timeDelta;
    }

    public Velocity flipYAxis() {
        double x = getX() * getTimeDelta();
        double y = -getY() * getTimeDelta();

        return new Velocity(x, y, getTimeDelta(), getEstimatedFrames());
    }

}
