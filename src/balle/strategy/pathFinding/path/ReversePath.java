package balle.strategy.pathFinding.path;

import balle.strategy.curve.Curve;

/**
 * normal paths assume the origional orientation is forward... this will take any path that was created with the opposite assumption and make the robot move backwards.
 * 
 * All this really does is negate and swap the wheel velocities
 * @author s0906575
 *
 */
public class ReversePath extends AbstractPath {

	private Path reversePath;

	public ReversePath(Path reversePath) {
		super(reversePath.getCurve());
		this.reversePath = reversePath;
	}
	
	@Override
	public double[] getVelocities(double t, Curve c, double leftWheelVel,
			double rightWheelVel) {
		double[] adjustedVels = reversePath.getVelocities(t, c, -rightWheelVel,
				-leftWheelVel);
		double[] actualVels = new double[] { -adjustedVels[1], -adjustedVels[0] };
		return actualVels;
	}

}
