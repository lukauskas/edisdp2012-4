package balle.world;

public class Robot extends FieldObject {

    private final double orientation;

    public double getOrientation() {
        return orientation;
    }

    public Robot(Coord position, double angle, double velocity, double facing) {
        super(position, angle, velocity);
        this.orientation = facing;
    }

}
