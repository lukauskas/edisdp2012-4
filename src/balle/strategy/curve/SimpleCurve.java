package balle.strategy.curve;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import balle.world.Coord;
import balle.world.Scaler;

public abstract class SimpleCurve implements Curve {

	/**
	 * Control points.
	 */
	protected final Coord[] p;

	protected SimpleCurve(Coord[] p) {
		this.p = p;
	}

	@Override
	public void draw(Graphics g, Scaler s) {
		g.setColor(Color.WHITE);

		ArrayList<Coord> coords = new ArrayList<Coord>();
		for (double t = -0.1; t < 1.1; t += 0.01)
			coords.add(pos(t));

		for (Coord c : coords) {
			int x, y, w = 2;
			x = (int) (s.m2PX(c.getX()) - (w / 2));
			y = (int) (s.m2PY(c.getY()) - (w / 2));
			g.fillOval(x, y, w, w);
		}
	}

}
