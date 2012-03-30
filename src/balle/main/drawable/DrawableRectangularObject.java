package balle.main.drawable;

import java.awt.Color;
import java.awt.Graphics;

import balle.world.Scaler;
import balle.world.objects.RectangularObject;

public class DrawableRectangularObject implements Drawable {

    private final RectangularObject obj;
    private Color color;

    public DrawableRectangularObject(RectangularObject obj, Color color) {
        super();
        this.obj = obj;
        this.color = color;
    }

    @Override
    public void draw(Graphics g, Scaler s) {
        DrawableLine front = new DrawableLine(obj.getFrontSide(), color);
        DrawableLine back = new DrawableLine(obj.getBackSide(), color);
        DrawableLine left = new DrawableLine(obj.getLeftSide(), color);
        DrawableLine right = new DrawableLine(obj.getRightSide(), color);

        front.draw(g, s);
        back.draw(g, s);
        left.draw(g, s);
        right.draw(g, s);
    }

    @Override
    public void reduceVisibility() {
        color = new Color(color.getRed(), color.getGreen(), color.getBlue(),
                color.getAlpha() / 10);
    }

}
