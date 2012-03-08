package balle.main.drawable;

import java.awt.Color;
import java.awt.Graphics;

import balle.world.Coord;
import balle.world.Scaler;

public class Circle extends Dot {

	private double r;

	public Circle(double x, double y, double r, Color colour) {
		super(x, y, colour);
		this.r = r;
	}

	public Circle(Coord coordinate, double r, Color colour) {
		this(coordinate.getX(), coordinate.getY(), r, colour);
	}

	@Override
	public void draw(Graphics g, Scaler scaler) {
		g.setColor(getColour());

		int a, b, c, d;
		int sr = (int) (scaler.getScale() * r);
		a = (int) (scaler.m2PX(getX()) - sr);
		b = (int) (scaler.m2PY(getY()) - sr);
		c = d = 2 * sr;

		g.drawOval(a, b, c, d);

	}
}
