package balle.strategy.curve;

import java.util.ArrayList;
import java.util.Stack;

import balle.world.Coord;
import balle.world.Orientation;

public class FiniteDifferenceCHI extends CubicHermiteInterpolator {

	@Override
	public ArrayList<Bezier4> getCurves(Stack<Coord> controlPoints,
			Orientation start,
			Orientation end) {
		int cpLength = controlPoints.size();

		ArrayList<Bezier4> curves = new ArrayList<Bezier4>();

		double div = 1.0 / ((double) cpLength - 1);

		for (int i = 0; i < cpLength - 1; i++) {
			Coord m0 = null, m1 = null;

			if (i == 0) {
				m0 = new Coord(
						controlPoints.get(0).dist(controlPoints.get(1)) / 4, 0)
						.rotate(start);
			} else {
				m0 = m(controlPoints.get(i - 1), div * (i - 1),
						controlPoints.get(i), div * i,
						controlPoints.get(i + 1), div * (i + 1));
			}

			if (i + 2 == cpLength) {
				m1 = new Coord(
-controlPoints.get(cpLength - 2).dist(
						controlPoints.get(cpLength - 1)) / 4,
						0).rotate(end);
			} else {
				m1 = m(controlPoints.get(i), div * i, controlPoints.get(i + 1),
						div * (i + 1), controlPoints.get(i + 2), div * (i + 2));
			}

			System.out.println(m0 + " " + m1);

			Coord[] out = new Coord[] { 
 controlPoints.get(i),
					controlPoints.get(i).add(m0.div(3)),
					controlPoints.get(i + 1).sub(m1.div(3)),
					controlPoints.get(i + 1), };

			curves.add(new Bezier4(out));
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
