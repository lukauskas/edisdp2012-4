package balle.world;

import java.awt.geom.Line2D;

public class Line {

	private final Coord a, b;

	public Coord getA() {
		return a;
	}

	public Coord getB() {
		return b;
	}

	public Line(double x1, double y1, double x2, double y2) {
		this.a = new Coord(x1, y1);
		this.b = new Coord(x2, y2);
	}

	public Line(Coord a, Coord b) {
		this.a = a;
		this.b = b;
	}

	public boolean contains(Coord a) {
		double dist = getLine2DVersion().ptLineDist(a.getX(), a.getY());
		if (dist > 0.000001) {
			return false;
		}

		return (((minX() - a.getX() < 0.00001) && (maxX() - a.getX()) > -0.00001) && ((minY()
				- a.getY() < 0.00001) && (maxY() - a.getY() > -0.00001)));

	}

	/**
	 * rotate the line around the origin
	 * 
	 * @param orientation
	 * @return
	 */
	public Line rotate(Orientation orientation) {
		return new Line(a.rotate(orientation), b.rotate(orientation));
	}

	public Line add(Coord position) {
		return new Line(a.add(position), b.add(position));
	}

	public Coord getIntersect(Line l) {
		double x1, x2, y1, y2;

		x1 = a.getX();
		y1 = a.getY();
		x2 = b.getX();
		y2 = b.getY();
		double a1 = y2 - y1;
		double b1 = x1 - x2;
		double c1 = (a1 * x1) + (b1 * y1);

		x1 = l.getA().getX();
		y1 = l.getA().getY();
		x2 = l.getB().getX();
		y2 = l.getB().getY();
		double a2 = y2 - y1;
		double b2 = x1 - x2;
		double c2 = (a2 * x1) + (b2 * y1);

		double det = a1 * b2 - a2 * b1;
		if (det == 0) {
			// lines are parallel
			return null;
		} else {
			double x = (b2 * c1 - b1 * c2) / det;
			double y = (a1 * c2 - a2 * c1) / det;
			Coord p = new Coord(x, y);
			if (contains(p) && l.contains(p)) {
				return p;
			} else {
				return null;
			}
		}

	}

	public boolean overLaps(Line l) {
		return false;
	}

	public boolean intersects(Line l) {
		return getIntersect(l) != null;
	}

	/**
	 * Returns the midpoint of the line
	 * 
	 * @return
	 */
	public Coord midpoint() {
		return new Coord((getA().getX() + getB().getX()) / 2.0,
				(getA().getY() + getB().getY()) / 2.0);
	}

	private Line2D getLine2DVersion() {
		return new Line2D.Double(a.getX(), a.getY(), b.getX(), b.getY());
	}

	public double minX() {
		return Math.min(getA().getX(), getB().getX());
	}

	public double maxX() {
		return Math.max(getA().getX(), getB().getX());
	}

	public double minY() {
		return Math.min(getA().getY(), getB().getY());
	}

	public double maxY() {
		return Math.max(getA().getY(), getB().getY());
	}

	/**
	 * Returns a point that the closest point on the line segment to the given
	 * point.
	 * 
	 * @param p
	 *            the point provided
	 * @return the closest point
	 */
	public Coord closestPoint(Coord p) {

		Coord AP = p.sub(getA());
		Coord AB = getB().sub(getA());

		double t = AB.sqrAbs() / AP.dot(AB);

		Coord closest = new Coord(getA().getX() + AB.getX() * t, getA().getY()
				+ AB.getY() * t);
		// Check if the closest point is in the segment
		if (this.contains(closest)) {
			return closest;
		} else {
			if (getA().dist(p) < getB().dist(p))
				return getA();
			else
				return getB();
		}
	}
}
