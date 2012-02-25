package balle.main;

import java.awt.Color;

public class Drawable {

    public static final int POINT = 0;
    private int             type;
    private double          x;
    private double          y;
    private Color           colour;

    public Drawable(int type, double d, double e, Color colour) {

        this.type = type;
        this.x = d;
        this.y = e;
        this.colour = colour;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

}
