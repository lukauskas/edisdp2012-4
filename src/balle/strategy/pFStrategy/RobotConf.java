package balle.strategy.pFStrategy;

//Robot configuration class. 
//b: distance between two wheels.
//r: radius of the wheel.
public class RobotConf {
	private double distance;
	private double radius;

	public RobotConf(double dist, double rad) {
		this.distance = dist;
		this.radius= rad;
	}

	public double getb() {
		return distance;
	}

	public double getr() {
		return radius;
	}
}
