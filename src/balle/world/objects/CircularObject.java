package balle.world.objects;

import balle.misc.Globals;
import balle.world.Coord;
import balle.world.Line;
import balle.world.Velocity;

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
    public boolean isNearWall(Pitch p) {
        return isNearWall(p, Globals.DISTANCE_TO_WALL);

    }

    public boolean isNearWall(Pitch p, double distance) {
        /*
         * Defines imaginary rectangle inside the pitch with size dependent on
         * the DISTANCE_TO_WALL constant. If the robot is within this rectangle
         * it is considered to be away from any walls.
         */

        double minX, maxX, minY, maxY;
        minX = p.getMinX() + distance;
        maxX = p.getMaxX() - distance;
        minY = p.getMinY() + distance;
        maxY = p.getMaxY() - distance;

        if (getPosition().getX() < minX)
            return true;
        if (getPosition().getX() > maxX)
            return true;
        if (getPosition().getY() < minY)
            return true;
        if (getPosition().getY() > maxY)
            return true;

        return false;

    }

    /**
     * Checks if ball is near the robot
     * 
     * @param r
     *            the robot
     * @return true, if it is near
     */
    public boolean isNear(Robot r) {
        if ((getPosition() == null) || (r.getPosition() == null))
            return false;
        return getPosition().dist(r.getPosition()) <= Math.max(r.getHeight(),
                r.getWidth())
                + getRadius() + 0.05;
    }

    @Override
    public boolean isInCorner(Pitch p) {
        Coord c[] = new Coord[4];
        c[0] = new Coord(p.getMinX(), p.getMinY());
        c[1] = new Coord(p.getMinX(), p.getMaxY());
        c[2] = new Coord(p.getMaxX(), p.getMinY());
        c[3] = new Coord(p.getMaxX(), p.getMaxY());

        for (Coord each : c)
            each = each.sub(getPosition());

        for (Coord each : c)
            if (each.abs() < Globals.DISTANCE_TO_CORNER)
                return true;

        return false;
    }

    /*
     * Checks if the circular object intersects the line given by the start and
     * end points A and B
     */
    @Override
    public boolean intersects(Line line) {

        double x = line.getB().getX() - line.getA().getX();
        double y = line.getB().getY() - line.getA().getY();
        double r = Math.sqrt(x * x + y * y);
        double D = line.getA().getX() * line.getB().getY() - line.getB().getX()
                * line.getA().getY();

        double Delta = this.getRadius() * this.getRadius() * (r * r) - D * D;

        if (Delta < 0)
            return false;
        if (Delta > 0)
            return true;
        if (Delta == 0)
            return true; // Line is tangent to the object

        return false;
    }
}
