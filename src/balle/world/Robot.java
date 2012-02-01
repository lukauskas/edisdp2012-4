package balle.world;

public class Robot extends FieldObject {

    private final double orientation;

    public double getOrientation() {
        return orientation;
    }

    public Robot(Coord position, Velocity velocity, double orientation) {
        super(position, velocity);
        this.orientation = orientation;
    }

}
