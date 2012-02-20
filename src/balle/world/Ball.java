package balle.world;

import balle.misc.Globals;

public class Ball extends CircularObject implements FieldObject {

    public Ball(Coord position, Velocity velocity) {
        super(position, velocity, Globals.BALL_RADIUS);
    }

}
