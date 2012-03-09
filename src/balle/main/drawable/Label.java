package balle.main.drawable;

import java.awt.Color;
import java.awt.Graphics;

import balle.world.Coord;
import balle.world.Scaler;

public class Label implements Drawable {

    public final String str;
    public final Coord position;
    public final Color color;

    public Label(String str, Coord position, Color color) {
        super();
        this.str = str;
        this.position = position;
        this.color = color;
    }


    @Override
    public void draw(Graphics g, Scaler s) {
        g.setColor(color);
        g.drawString(str, s.m2PX(position.getX()), s.m2PY(position.getY()));
    }

}
