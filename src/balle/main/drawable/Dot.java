package balle.main.drawable;

import java.awt.Color;
import java.awt.Graphics;

import balle.world.Coord;
import balle.world.Scaler;

public class Dot implements Drawable {

    private double x;
    private double y;
    private Color  colour;

    public Dot(double d, double e, Color colour) {
        this.x = d;
        this.y = e;
        this.colour = colour;
    }

    public Dot(Coord coordinate, Color colour) {
        this(coordinate.getX(), coordinate.getY(), colour);
    }

    public double getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Color getColour() {
        return colour;
    }

    public void setColour(Color colour) {
        this.colour = colour;
    }

    @Override
    public void draw(Graphics g, Scaler scaler) {
        float w = 5;
        g.setColor(getColour());
        g.fillOval((int) (scaler.m2PX(getX()) - (w / 2)), (int) (scaler.m2PY(getY()) - (w / 2)),
                (int) w, (int) w);

    }

    @Override
    public void reduceVisibility() {
        Color currentColour = getColour();
        // Reduce the alpha by 1/10
        Color newColour = new Color(currentColour.getRed(),
                currentColour.getGreen(), currentColour.getBlue(),
                currentColour.getAlpha() / 10);

        setColour(newColour);

    }

}
