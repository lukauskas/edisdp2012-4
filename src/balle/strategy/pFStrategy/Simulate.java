package balle.strategy.pFStrategy;

//Naive forward kinematic implementation for simulating robot movement.
public class Simulate {
	private double b;
	private Pos position;

	public Simulate(Pos initPosition, double b) {
		position = initPosition;
		this.b = b;
	}

	public Simulate(double b) {
		this(new Pos(new Point(0, 0), 0), b);
	}

	public Pos move(double leftV, double rightV, double time) {
		double theta = (rightV - leftV) * time / b + position.getAngle();
		double S = (rightV + leftV) / 2;
		double x = S * Math.cos(theta) * time + position.getLocation().getX();
		double y = S * Math.sin(theta) * time + position.getLocation().getY();
		position = new Pos(new Point(x, y),theta);
		return position;

	}
}
