package balle.world.objects;

import balle.misc.Globals;
import balle.world.Coord;
import balle.world.Velocity;

public class Ball extends CircularObject implements FieldObject {

    public Ball(Coord position, Velocity velocity) {
        super(position, velocity, Globals.BALL_RADIUS);
    }

}
