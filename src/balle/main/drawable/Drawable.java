package balle.main.drawable;

import java.awt.Graphics;

import balle.world.Scaler;

public interface Drawable {
    public void draw(Graphics g, Scaler s);

    /**
     * Reduce the visibility of Drawable object. E.g. we want the opponent
     * drawables have a lower alpha-value than our.
     */
    public void reduceVisibility();

}
