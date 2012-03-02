package balle.strategy.curve;

import java.util.ArrayList;
import java.util.Stack;

import balle.world.Coord;
import balle.world.Orientation;

public class CustomCHI extends CubicHermiteInterpolator {

	@Override
	public ArrayList<Bezier4> getCurves(Stack<Coord> controlPoints,
			Orientation start,
			Orientation end) {
		ArrayList<Bezier4> curves = new ArrayList<Bezier4>();

		Orientation inc = null;

		while (controlPoints.size() >= 2) {
			Coord[] p = new Coord[4];

			p[3] = controlPoints.pop();
			p[0] = controlPoints.peek();

			double distS = p[0].dist(p[3]) / 4;

			// Calculate p0.
			if (inc == null)
				p[2] = p[3].add(end.getOpposite().getUnitCoord().mult(distS));
			else
				p[2] = p[3].add(inc.getOpposite().getUnitCoord().mult(distS));

			// Choose a value for inc.

			// Calculate p1.
			if (inc == null)
				p[1] = p[0].add(start.getUnitCoord().mult(distS));
			else
				p[1] = p[0].add(inc.getUnitCoord().mult(distS));

			curves.add(0, new Bezier4(p));
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
		return (next.sub(curr).mult(1 / (2 * (tNext - tCurr))).add(curr.sub(
				prev).mult(1 / (2 * (tCurr - tPrev))))).getUnitCoord().mult(
				prev.dist(curr));
	}
}
