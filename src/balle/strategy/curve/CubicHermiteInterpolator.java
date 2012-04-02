package balle.strategy.curve;

import java.util.ArrayList;
import java.util.Stack;

import balle.world.Coord;
import balle.world.Orientation;

public abstract class CubicHermiteInterpolator implements Interpolator {

	@Override
	public Spline getCurve(Coord[] controlPoints, Orientation start,
			Orientation end) {
		Stack<Coord> stack = new Stack<Coord>();
		for (Coord c : controlPoints)
			stack.push(c);
		ArrayList<Bezier4> beziers = getCurves(stack, start, end);
		Bezier4[] array = new Bezier4[beziers.size()];
		for (int i = 0; i < array.length; i++)
			array[i] = beziers.get(i);
		return new Spline(array);
	}

	public abstract ArrayList<Bezier4> getCurves(Stack<Coord> controlPoints,
			Orientation start, Orientation end);

}
