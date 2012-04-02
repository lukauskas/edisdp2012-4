package balle.strategy.bezierNav.neural;

public class NeuralObj {
	
	protected final float performance;

	protected final double currLeft, currRight;
	protected final int desLeftCmd, desRightCmd;
	
	public NeuralObj(int desLeftCmd, int desRightCmd, double currLeft,
			double currRight, float performance) {
		this.desLeftCmd = desLeftCmd;
		this.desRightCmd = desRightCmd;
		this.currLeft = currLeft;
		this.currRight = currRight;
		this.performance = performance;
	}

	public float getPerformance() {
		return performance;
	}

	public double getCurrLeft() {
		return currLeft;
	}

	public double getCurrRight() {
		return currRight;
	}

	public int getDesLeftCmd() {
		return desLeftCmd;
	}

	public int getDesRightCmd() {
		return desRightCmd;
	}

}
