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
		int cpLength = controlPoints.size();

		ArrayList<Bezier4> curves = new ArrayList<Bezier4>();

		double div = 1.0 / ((double) cpLength - 1);

		while (controlPoints.size() >= 2) {
			Coord[] coords = new Coord[4];
			coords[3] = controlPoints.pop();
			coords[0] = controlPoints.peek();


			curves.add(0, new Bezier4(coords));
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
