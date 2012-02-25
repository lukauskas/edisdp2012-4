package balle.strategy.pFStrategy;

//basic support for basic vector operations.
public class Vector extends Point {

    public Vector(double x, double y) {
        super(x, y);

    }

    public Vector(Point point) {
        super(point.getX(), point.getY());
    }

    public Vector add(Vector vector) {
        return new Vector(this.getX() + vector.getX(), this.getY()
                + vector.getY());
    }

    public Vector subtract(Vector vector) {
        return new Vector(this.getX() - vector.getX(), this.getY()
                - vector.getY());
    }

    public Vector mult(double scalar) {
        return new Vector(this.getX() * scalar, this.getY() * scalar);
    }

    public double normalAngle() {
        if (Math.atan2(getY(), getX()) >= 0)
            return Math.atan2(getY(), getX());
        else
            return 2 * Math.PI + Math.atan2(getY(), getX());
    }

    public double norm() {
        return Math.sqrt(getY() * getY() + getX() * getX());
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
