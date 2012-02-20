package balle.world;

import balle.misc.Globals;

public class Robot extends RectangularObject {

    public Robot(Coord position, Velocity velocity, Orientation orientation) {
        super(position, velocity, orientation, Globals.ROBOT_WIDTH, Globals.ROBOT_LENGTH);
    }
}
