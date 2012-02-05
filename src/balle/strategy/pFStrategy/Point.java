package balle.strategy.pFStrategy;

//basic Point class includes x,y 
public class Point {
	private final double x;
	private final double y;

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;

	}

	public double getY() {
		return y;
	}

	@Override
	public String toString() {
		return "X: " + String.valueOf(x) + ", Y:" + String.valueOf(y);
	}
}
