package balle.world.objects;

import java.awt.geom.Rectangle2D;

import org.apache.log4j.Logger;

import balle.misc.Globals;
import balle.world.AngularVelocity;
import balle.world.Coord;
import balle.world.Line;
import balle.world.Orientation;
import balle.world.Velocity;

public class RectangularObject extends MovingPoint implements FieldObject {

    private static final Logger LOG = Logger.getLogger(RectangularObject.class);

    private final double width;
    private final double height;
    
    private final Orientation orientation;
	private final AngularVelocity angularVelocity;

    public RectangularObject(Coord position, Velocity velocity,
			AngularVelocity angularVelocity,
            Orientation orientation, double width, double height) {
        super(position, velocity);
        this.width = width;
        this.height = height;
        this.orientation = orientation;
		this.angularVelocity = angularVelocity;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Orientation getOrientation() {
        return orientation;
    }

	public AngularVelocity getAngularVelocity() {
		return angularVelocity;
	}

    @Override
    public boolean containsCoord(Coord point) {
        if ((getPosition() == null) || (point == null))
            return false;

        Rectangle2D rect = new Rectangle2D.Double(getPosition().getX() - width
                / 2, getPosition().getY() - height / 2, width, height);

        return rect.contains(point.getX(), point.getY());
    }

    @Override
    public boolean isNearWall(Pitch p) {
        return isNearWall(p, Globals.DISTANCE_TO_WALL);
    }

    public boolean isNearWall(Pitch p, double epsilon) {
        /*
         * Defines imaginary rectangle inside the pitch with size dependent on
         * the DISTANCE_TO_WALL constant. If the robot is within this rectangle
         * it is considered to be away from any walls.
         */

        for (Line wall : p.getWalls()) {
            if (wall.dist(getPosition()) < epsilon) {
                return true;
            }
        }

        return false;
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

    public Line getBackSide() {
        double hw = getWidth() / 2;
        double hh = getHeight() / 2;
        Line c = new Line(-hh, -hw, -hh, hw);
        c = c.rotate(getOrientation());
        c = c.add(getPosition());
        return c;
    }

    public Line getFrontSide() {
        double hw = getWidth() / 2;
        double hh = getHeight() / 2;
        Line a = new Line(hh, hw, hh, -hw);
        Orientation o = getOrientation();
        a = a.rotate(o);
        Coord p = getPosition();
        a = a.add(p);
        return a;
    }

    public Line getLeftSide() {
        double hw = getWidth() / 2;
        double hh = getHeight() / 2;
        Line d = new Line(-hh, hw, hh, hw);
        Orientation o = getOrientation();
        d = d.rotate(o);
        Coord p = getPosition();
        d = d.add(p);
        return d;
    }

    public Line getRightSide() {
        double hw = getWidth() / 2;
        double hh = getHeight() / 2;
        Line b = new Line(hh, -hw, -hh, -hw);
        Orientation o = getOrientation();
        b = b.rotate(o);
        Coord p = getPosition();
        b = b.add(p);
        return b;
    }

    @Override
    public boolean intersects(Line line) {
        if (line == null)
            return false;
        if (getPosition() == null)
            return false;

        if (containsCoord(line.getA()) || containsCoord(line.getB()))
            return true;

        Line a = getLeftSide();
        Line b = getRightSide();
        Line c = getBackSide();
        Line d = getFrontSide();

        // if the line l intersects any of the lines connecting the corners of
        // this rectangle, then return true;
        return line.intersects(a) || line.intersects(b) || line.intersects(c)
                || line.intersects(d);
    }

}
