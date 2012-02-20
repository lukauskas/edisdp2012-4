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
        // TODO Implement this function!
        // Should check the orientation, width and height.

        return false;
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
