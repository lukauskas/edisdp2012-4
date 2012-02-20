package balle.world;

public class CircularObject extends MovingPoint implements FieldObject {

    private final double radius;

    public CircularObject(Coord position, Velocity velocity, double radius) {
        super(position, velocity);
        this.radius = radius;
    }

    @Override
    public boolean containsCoord(Coord point) {
        return getPosition().dist(point) <= getRadius();
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public boolean isNearWall() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isInCorner() {
        // TODO Auto-generated method stub
        return false;
    }
}
