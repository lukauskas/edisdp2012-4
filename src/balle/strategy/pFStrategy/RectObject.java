package balle.strategy.pFStrategy;

//A rectangle based object which calculates repulsive vector given that a surface is 
//considered.
public class RectObject implements PFObject {

	private final double power;
	private final double infl_distance;
	private final Point p1;
	private final Point p2;

	public RectObject(Point p1, Point p2, double power, double infl_distance) {
		this.p1 = p1;
		this.p2 = p2;
		this.power = power;
		this.infl_distance = infl_distance;

	}

	public Point getp1() {
		return p1;
	}

	public Point getp2() {
		return p2;
	}

	@Override
	public Vector getVector(Point point, boolean repulsive) {

		if ((point.getX() < p1.getX() && point.getX() > p2.getX())
				|| (point.getX() > p1.getX() && point.getX() < p2.getX())) {
			PointObject obj = new PointObject(point.getX(), (p1.getY() + p2
					.getY()) / 2, power, infl_distance);
			return obj.getVector(point, repulsive);
		} else if ((point.getY() < p1.getY() && point.getY() > p2.getY())
				|| (point.getY() > p1.getY() && point.getY() < p2.getY())) {
			PointObject obj = new PointObject((p1.getX() + p2.getX()) / 2,
					point.getY(), power, infl_distance);
			return obj.getVector(point, repulsive);
		} else {
			return new Vector(0, 0);

		}
	}

	@Override
	public String toString() {
		return "Point1: " + p1.toString() + ",Point2: " + p2.toString();
	}

	// This is getVector function for rectangle shaped object of extended
	// potential field. This is going to be implemented in the future.
	// for the time being it simply returns normal PF vecotr.
	@Override
	public Vector getVector(Pos point, boolean repulsive) {

		return getVector(point.getLocation(), repulsive);

	}

}
