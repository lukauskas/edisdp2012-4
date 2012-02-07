package balle.world;

public class Robot extends FieldObject {

    private final Orientation orientation;

    public Robot(Coord position, Velocity velocity, Orientation orientation) {
        super(position, velocity);
        this.orientation = orientation;
    }

    public Orientation getOrientation() {
        return orientation;
    }

}
