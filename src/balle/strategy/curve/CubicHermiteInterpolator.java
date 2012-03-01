package balle.strategy.curve;

import static java.lang.Math.pow;
import balle.world.Coord;

public class CubicHermiteInterpolator {

	public Curve getCurve(Coord[] controlPoints) {
		Curve[] curves = new Curve[controlPoints.length - 1];
		for (int i = 0; i < controlPoints.length - 1; i++) {

			Coord[] out = new Coord[] { pts[i], m0(pts, i), m1(pts, i),
					pts[i + 1], };

			curves[i] = out;
		}
		return new Spline(curves);
	}

	protected Curve getCurve(Coord[] pts, int i) {

		return new Bezier4(pts);
	}

	protected double finiteDifference(Coord prev, Coord curr, Coord next) {
		return 0;
	}

	// Unused helper functions.

	protected double h00(double t) {
		return 2 * pow(t, 3) - 3 * pow(t, 2) + 1;
	}

	protected double h10(double t) {
		return pow(t, 3) - 2 * pow(t, 2) + t;
	}

	protected double h01(double t) {
		return -2 * pow(t, 3) + 3 * pow(t, 2);
	}

	protected double h11(double t) {
		return pow(t, 3) - pow(t, 2);
	}
}
