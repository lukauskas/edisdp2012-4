package balle.world;

public class Coord {

    private final double x;
    private final double y;

    private boolean      estimated = false;

    public Coord(double x, double y) {
        super();
        this.x = x;
        this.y = y;
    }

    public Coord(double x, double y, boolean estimated) {
        this(x, y);
        this.estimated = estimated;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double abs() {
        return Math.sqrt(x * x + y * y);
    }

    public Coord sub(Coord c) {
        return new Coord(x - c.getX(), y - c.getY());
    }

    public Coord add(Coord c) {
        return new Coord(x + c.getX(), y + c.getY());
    }

    public double dist(Coord c) {
        return c.sub(this).abs();
    }

    /**
     * Returns whether the coordinates have been estimated or not. By convention
     * the coordinates from the vision are not estimated, whereas the
     * coordinates that are updated from velocities e.g. when the vision returns
     * -1 are.
     * 
     * @return true or false depending whether the coordinates were estimated or
     *         not
     */
    public boolean isEstimated() {
        return estimated;
    }
}
