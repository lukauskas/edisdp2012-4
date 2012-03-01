package balle.strategy.curve;

import java.awt.Graphics;

import balle.world.Coord;
import balle.world.Scaler;

public class Spline implements Curve {

	private final Curve[] splines;
	
	public Spline(Curve[] splines) {
		this.splines = splines;
	}

	@Override
	public Coord pos(double t) {
		return null;
	}

	@Override
	public Coord vel(double t) {
		return null;
	}

	@Override
	public Coord acc(double t) {
		return null;
	}

	@Override
	public void draw(Graphics g, Scaler s) {
		for (Curve spline : splines)
			spline.draw(g, s);
	}
}
