package balle.strategy.curve;

import balle.world.Coord;

public class CubicHermiteInterpolator {

	public Curve getCurve(Coord[] controlPoints) {
		Curve[] curves = new Curve[controlPoints.length - 1];
		for (int i = 1; i < controlPoints.length - 2; i++) {
			Coord m0 = null, m1 = null;

			if (i == 0) {
				// m0 is special case.
				m1 = m(true, controlPoints[i], controlPoints[i + 1],
						controlPoints[i + 2]);
			} else if (i + 1 == controlPoints.length) {
				m0 = m(false, controlPoints[i - 1], controlPoints[i],
						controlPoints[i + 1]);
				// m1 is special case.
			} else {
				m0 = m(false, controlPoints[i - 1], controlPoints[i],
						controlPoints[i + 1]);
				m1 = m(true, controlPoints[i], controlPoints[i + 1],
						controlPoints[i + 2]);
			}

			Coord[] out = new Coord[] { 
					controlPoints[i], 
					controlPoints[i].add(m0.mult(1 / 3)),
					controlPoints[i + 1].sub(m1.mult(1 / 3)),
					controlPoints[i + 1], };

			curves[i] = new Bezier4(out);
		}
		return new Spline(curves);
	}

	/**
	 * Calculates the
	 * 
	 * @param prev
	 *            Previous control coordinate.
	 * @param curr
	 *            Current control coordinate.
	 * @param next
	 *            Next control coordinate.
	 * @return Coordinate for controlling angle/tangent of the spline.
	 */
	protected Coord m(boolean t, Coord prev, Coord curr, Coord next) {
		int t0, t1;
		if (t) {
			t0 = 1;
			t1 = 0;
		} else {
			t0 = 0;
			t1 = 1;
		}
		return next.sub(curr).mult(1 / (2 * (t1 - t0)))
				.add(curr.sub(prev).mult(1 / (2 * (t0 - t1))));
	}

	protected double t(double x, double xk, double xl) {
		return (x - xk) / (xl - xk);
	}

}
