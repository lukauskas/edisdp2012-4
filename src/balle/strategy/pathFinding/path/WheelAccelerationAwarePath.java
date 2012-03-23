package balle.strategy.pathFinding.path;

import balle.strategy.curve.Curve;

public abstract class WheelAccelerationAwarePath extends AbstractPath {

	private static final double EXAGGERATION = 1.5;

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
		vels[0] += EXAGGERATION + dVels[0];
		vels[1] += EXAGGERATION + dVels[1];
		return vels;
	}

}
