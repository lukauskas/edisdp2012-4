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
}
