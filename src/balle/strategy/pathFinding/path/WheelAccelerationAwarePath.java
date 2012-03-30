package balle.strategy.pathFinding.path;

import balle.strategy.curve.Curve;

public class WheelAccelerationAwarePath extends AbstractPath {

	private static final double EXAGGERATION = 4;
    private static final double MOISTURIZER = 0.3;

	private Path accelIgnorantPath;

	public WheelAccelerationAwarePath(Path accelIgnorantPath) {
		super(accelIgnorantPath.getCurve());
		this.accelIgnorantPath = accelIgnorantPath;
	}

	@Override
	public double[] getVelocities(double t, Curve c, double leftWheelVel,
			double rightWheelVel) {


		// get the ignorant velocities
		double[] vels = accelIgnorantPath.getVelocities(t, c, leftWheelVel,
				rightWheelVel);
		// find the change in velocity needed
		double[] dVels = new double[] { vels[0] - leftWheelVel,
				vels[1] - rightWheelVel };
		// exaggerate
		double[] newVels = new double[2];
		newVels[0] = leftWheelVel + (EXAGGERATION * dVels[0]);
		newVels[1] = rightWheelVel + (EXAGGERATION * dVels[1]);

		newVels[0] = (((1 - MOISTURIZER) * newVels[0]) + (MOISTURIZER * leftWheelVel));
		newVels[1] = (((1 - MOISTURIZER) * newVels[1]) + (MOISTURIZER * rightWheelVel));

		return newVels;
	}

}
