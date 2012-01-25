package balle.world;

public class Coord {

	private double x;
	private double y;

	public Coord(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public double abs() {
		return Math.sqrt(x*x + y*y);
	}
	
	public Coord sub(Coord c) {
		return new Coord(x-c.x, y-c.y);
	}
	
	public Coord add(Coord c) {
		return new Coord(x+c.x, y+c.y);
	}
	
	public double dist(Coord c) {
		return c.sub(this).abs();
	}
	

}
