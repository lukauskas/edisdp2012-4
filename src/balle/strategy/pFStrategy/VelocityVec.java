package main.strategy.pFStrategy;

//a Velocity vector that includes left and right wheels.
public class VelocityVec extends Vector {

	public VelocityVec(double left, double right) {
		super(left, right);

	}

	public double getLeft() {
		return this.getX();
	}

	public double getRight() {
		return this.getY();
	}

}
