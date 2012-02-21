package balle.world.objects;

import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Velocity;

public class RectangularObject extends MovingPoint implements FieldObject {
    private final double      width;
    private final double      height;
    private final Orientation orientation;

    public RectangularObject(Coord position, Velocity velocity, Orientation orientation,
            double width, double height) {
        super(position, velocity);
        this.width = width;
        this.height = height;
        this.orientation = orientation;
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

    @Override
    public boolean containsCoord(Coord point) {
        Coord dPoint = new Coord(point.getX()-getPosition().getX(),
        							point.getY()-getPosition().getY());
        
        dPoint = dPoint.rotate(getOrientation());
        if (dPoint.getX() < (-width/2.0f)) return false;
        if (dPoint.getX() > (width/2.0f)) return false;
        if (dPoint.getY() < (-height/2.0f)) return false;
        if (dPoint.getY() > (height/2.0f)) return false;
        return true;
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
