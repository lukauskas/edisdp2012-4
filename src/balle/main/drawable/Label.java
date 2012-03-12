package balle.main.drawable;

import java.awt.Color;
import java.awt.Graphics;

import balle.world.Coord;
import balle.world.Scaler;

public class Label implements Drawable {

    public final String str;
    public final Coord position;
    public Color colour;

    public Color getColour() {
        return colour;
    }

    public void setColour(Color colour) {
        this.colour = colour;
    }

    public Label(String str, Coord position, Color color) {
        super();
        this.str = str;
        this.position = position;
        this.colour = color;
    }


    @Override
    public void draw(Graphics g, Scaler s) {
        g.setColor(colour);
        g.drawString(str, s.m2PX(position.getX()), s.m2PY(position.getY()));
    }

    @Override
    public void reduceVisibility() {
        Color currentColour = getColour();
        // Set the alpha to 0 so we do not draw it
        Color newColour = new Color(currentColour.getRed(),
                currentColour.getGreen(), currentColour.getBlue(), 0);

        setColour(newColour);
    }

}
