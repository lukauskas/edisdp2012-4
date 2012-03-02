package balle.strategy.curve;

import java.util.Stack;

import balle.world.Coord;
import balle.world.Orientation;

public class FiniteDifferenceCHI extends CubicHermiteInterpolator {

	@Override
	public Bezier4[] getCurves(Stack<Coord> controlPoints, Orientation start,
			Orientation end) {
		int cpLength = controlPoints.length;

		Bezier4[] curves = new Bezier4[cpLength - 1];
		double div = 1.0 / ((double) curves.length);


		for (int i = 0; i < controlPoints.length - 1; i++) {
			Coord m0 = null, m1 = null;

			if (i == 0) {
				m0 = new Coord(controlPoints[0].dist(controlPoints[1]) / 4, 0)
						.rotate(start);
			} else {
				m0 = m(controlPoints[i - 1], div * (i - 1), controlPoints[i],
						div * i, controlPoints[i + 1], div * (i + 1));
			}

			if (i + 2 == controlPoints.length) {
				m1 = new Coord(
						-controlPoints[cpLength - 2]
								.dist(controlPoints[cpLength - 1]) / 4,
						0).rotate(end);
			} else {
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
		return curves;
	}

	/**
	 * Calculates the
	 * 
	 * @param prevm
	 *            Previous control coordinate.
	 * @param curr
	 *            Current control coordinate.
	 * @param next
	 *            Next control coordinate.
	 * @return Coordinate for controlling angle/tangent of the spline.
	 */
	protected Coord m(Coord prev, double tPrev, Coord curr, double tCurr,
			Coord next, double tNext) {
		return (next.sub(curr).mult(1 / (2 * (tNext - tCurr)))
.add(curr.sub(
				prev).mult(1 / (2 * (tCurr - tPrev))))).getUnitCoord().mult(
				prev.dist(curr));
	}

}
