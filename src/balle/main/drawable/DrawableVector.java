package balle.main.drawable;

import java.awt.Color;

import balle.world.Coord;
import balle.world.Line;

public class DrawableVector extends DrawableLine {

    /**
     * Create new drawable vector
     * 
     * @param position
     *            position to draw the vector from
     * @param vector
     *            the vector to draw
     * @param colour
     *            colour you want the vector to be drawn
     */
    public DrawableVector(Coord position, Coord vector, Color colour) {
        super(new Line(position, position.add(vector)), colour);
    }
}
