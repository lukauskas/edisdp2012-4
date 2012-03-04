package balle.strategy.curve;

import static java.lang.Math.pow;
import balle.world.Coord;

public class Bezier4 extends SimpleCurve {

	public Bezier4(Coord[] coords) {
		super(coords);
	}

	@Override
	public Coord pos(double t) {
		return p[0].mult(pow(1 - t, 3)).add(p[1].mult(3 * pow(1 - t, 2) * t))
				.add(p[2].mult(3 * (1 - t) * t * t)).add(p[3].mult(pow(t, 3)));
	}

	@Override
	public Coord vel(double t) {
		return p[0].mult(-3 + (6 * t) - (3 * t * t))
				.add(p[1].mult(3 * (1 - (4 * t) + (3 * t * t))))
				.add(p[2].mult(3 * ((2 * t) - (3 * t * t))))
				.add(p[3].mult(3 * t * t));
	}
	
	@Override
	public Coord acc(double t) {
		return p[0].mult(6 - (6 * t)).add(p[1].mult(3 * (-4 + (6 * t))))
				.add(p[2].mult(3 * (2 - (6 * t)))).add(p[3].mult(6 * t));
	}

	@Override
	public Coord closestPoint(Coord c) {
		// TODO Auto-generated method stub
		return null;
	}
}
