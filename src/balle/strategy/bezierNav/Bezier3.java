package balle.strategy.bezierNav;

import static java.lang.Math.pow;
import balle.world.Coord;

public class Bezier3 {

	private Coord[] p;

	public Bezier3(Coord[] coords) {
		this.p = coords;
	}

	public Coord pos(double t) {
		return p[0].mult(pow(1 - t, 2)).add(p[1].mult(2 * t * (1 - t)))
				.add(p[2].mult(t * t));
	}

	public Coord vel(double t) {
		return p[1].sub(p[0]).mult(2 * (1 - t)).add(p[2].sub(p[1]).mult(2 * t));
	}

	public Coord acc(double t) {
		throw new UnsupportedOperationException();
		// TODO
		// Not exactly what the derivitive is yet,
		// I think it is constant.
	}
}
