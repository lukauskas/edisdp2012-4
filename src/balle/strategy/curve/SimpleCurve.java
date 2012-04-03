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
	public Coord cor(double t) {
		double f, f1, f11, g, g1, g11;
		f = pos(t).getX();
		f1 = vel(t).getX();
		f11 = acc(t).getX();
		g = pos(t).getY();
		g1 = vel(t).getY();
		g11 = acc(t).getY();
		return new Coord(
				f
						- ((((f1 * f1) + (g1 * g1)) * g1) / ((f1 * g11) - (f11 * g1))),
				g
						+ ((((f1 * f1) + (g1 * g1)) * f1) / ((f1 * g11) - (f11 * g1))));
	}

	@Override
	public double rad(double t) {
		return cor(t).dist(pos(t));
	}

	@Override
	public void draw(Graphics g, Scaler s) {
		g.setColor(Color.WHITE);

		ArrayList<Coord> coords = new ArrayList<Coord>();
		for (double t = 0.0; t <= 1.0; t += 0.01)
			coords.add(pos(t));

		for (int i = 0; i + 1 < coords.size(); i++) {
			Coord c1 = coords.get(i), c2 = coords.get(i + 1);

			int x1, y1, x2, y2;
			x1 = (int) (s.m2PX(c1.getX()));
			y1 = (int) (s.m2PY(c1.getY()));
			x2 = (int) (s.m2PX(c2.getX()));
			y2 = (int) (s.m2PY(c2.getY()));
			g.drawLine(x1, y1, x2, y2);
		}

		g.setColor(Color.RED);
		for (Coord c : p) {
			int x, y, w = 4;
			x = (int) (s.m2PX(c.getX()) - (w / 2));
			y = (int) (s.m2PY(c.getY()) - (w / 2));
			g.fillRect(x, y, w, w);
		}

	}

	@Override
	public Coord getStart() {
		return p[0];
	}

	@Override
	public Coord getEnd() {
		return p[p.length - 1];
	}

    @Override
    public void reduceVisibility() {
        // TODO: implement!!!
    }

}
