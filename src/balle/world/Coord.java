package balle.world;

public class Coord {

    private final double x;
    private final double y;

    public Coord(double x, double y) {
        super();
        this.x = x;
        this.y = y;
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

}
