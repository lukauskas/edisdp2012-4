package balle.strategy.curve;

import balle.world.Coord;
import balle.world.Orientation;

public class CubicHermiteInterpolator implements Interpolator {

	@Override
	public Curve getCurve(Coord[] controlPoints, Orientation start,
			Orientation end) {

		Curve[] curves = new Curve[controlPoints.length - 1];
		double div = 1.0 / ((double) curves.length);
		curves[0] = new Bezier3(new Coord[] { controlPoints[0],
				controlPoints[0].sub(m(controlPoints[0], 0, controlPoints[1],
						div, controlPoints[2], div * 2).div(3)),
				controlPoints[1], });

		for (int i = 1; i < controlPoints.length - 1; i++) {
			Coord m0 = null, m1 = null;

			if (i == 0) {
				m1 = m(controlPoints[i], div * i, controlPoints[i + 1], div
						* (i + 1), controlPoints[i + 2], div * (i + 2));
				m0 = m1;
			} else if (i + 2 == controlPoints.length) {
				m0 = m(controlPoints[i - 1], div * (i - 1), controlPoints[i],
						div * i, controlPoints[i + 1], div * (i + 1));
				m1 = m0;
			} else {
				m0 = m(controlPoints[i - 1], div * (i - 1), controlPoints[i],
						div * i, controlPoints[i + 1], div * (i + 1));
				m1 = m(controlPoints[i], div * i, controlPoints[i + 1], div
						* (i + 1), controlPoints[i + 2], div * (i + 2));
			}
			System.out.println(m0 + " " + m1);

			Coord[] out = new Coord[] { 
					controlPoints[i], 
					controlPoints[i].add(m0.div(3)),
					controlPoints[i + 1].sub(m1.div(3)),
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
	protected Coord m(Coord prev, double tPrev, Coord curr, double tCurr,
			Coord next, double tNext) {
		return next.sub(curr).mult(1 / (2 * (tNext - tCurr)))
				.add(curr.sub(prev).mult(1 / (2 * (tCurr - tPrev))));
	}

	protected double t(double x, double xk, double xl) {
		return (x - xk) / (xl - xk);
	}

}
