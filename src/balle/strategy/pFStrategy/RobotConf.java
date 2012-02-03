package main.strategy.pFStrategy;

//Robot configuration class. 
//b: distance between two wheels.
//r: radius of the wheel.
public class RobotConf {
	private double b;
	private double r;

	public RobotConf(double b, double r) {
		this.b = b;
		this.r = r;
	}

	public double getb() {
		return b;
	}

	public double getr() {
		return r;
	}
}
