package balle.strategy.curve;

import java.awt.Graphics;

import balle.world.Coord;
import balle.world.Scaler;

public class Spline implements Curve {

	private final Curve[] splines;
	
	public Spline(Curve[] splines) {
		this.splines = splines;
	}

	protected double div() {
		return 1.0 / ((double) splines.length);
	}

	@Override
	public Coord pos(double t) {
		return splines[(int) Math.floor(t / div())].pos((t % div()) / div());
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
