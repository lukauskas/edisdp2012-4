package balle.strategy.curve;

import static java.lang.Math.pow;
import balle.world.Coord;

public class Bezier3 extends SimpleCurve {

	public Bezier3(Coord[] coords) {
		super(coords);
	}

	@Override
	public Coord pos(double t) {
		return p[0].mult(pow(1 - t, 2)).add(p[1].mult(2 * t * (1 - t)))
				.add(p[2].mult(t * t));
	}

	@Override
	public Coord vel(double t) {
		return p[1].sub(p[0]).mult(2 * (1 - t)).add(p[2].sub(p[1]).mult(2 * t));
	}

	@Override
	public Coord acc(double t) {
		throw new UnsupportedOperationException();
		// TODO
		// Not exactly what the derivitive is yet,
		// I think it is constant.
	}

	@Override
	public Coord closestPoint(Coord c) {
		// TODO re-do this code, it is very crude
		// http://mathoverflow.net/questions/8983/closest-point-on-bezier-spline
		// Might be helpfull.

		Coord closest = null;
		for (double i = 0; i <= 1; i += 0.1) {
			Coord testing = pos(i);
			if (closest == null || c.dist(testing) < c.dist(closest))
				closest = testing;
		}
		return closest;
	}

	public double length() {
		double sum = 0;
		Coord last = pos(0);
		for (double i = 0.1; i <= 1; i += 0.1) {
			Coord curr = pos(i);
			sum += last.dist(curr);
			last = curr;
		}
		return sum;
	}
}
