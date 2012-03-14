package balle.world;

public class AngularVelocity {

	private final double angleInRadians;
	private final double deltaTime;

	/**
	 * Initialises orientation. The angle must be set to radians if useRadians
	 * is true else the angle should be set to radians
	 * 
	 * @param angle
	 * @param useRadians
	 * @param deltaTime
	 *            in MILLISECONDS!!!!!!!
	 */
	public AngularVelocity(double angle, double deltaTime, boolean useRadians) {
		if (useRadians)
			angleInRadians = angle;
		else
			angleInRadians = (angle * Math.PI) / 180;
		this.deltaTime = deltaTime;
	}

	/**
	 * Initialises angular velocity
	 * 
	 * @param angle
	 *            in radians
	 * 
	 * @param deltaTime
	 *            in milliseconds !!!!
	 */
	public AngularVelocity(double angle, double deltaTime) {
		this(angle, deltaTime, true);
	}

	/**
	 * Return the angle in radians
	 * 
	 * @return radians
	 */
	public double radians() {
		return angleInRadians / deltaTime;
	}

	/**
	 * Return the angle in degrees per millisecond
	 * 
	 * @return angle in degrees
	 */
	public double degrees() {
		return (angleInRadians * 180) / (Math.PI * deltaTime);
	}

	public AngularVelocity sub(AngularVelocity targetOrientation) {
		double angle = targetOrientation.angleInRadians
				* targetOrientation.deltaTime / deltaTime;
		return new AngularVelocity(angleInRadians - angle, deltaTime);
	}

	public AngularVelocity add(AngularVelocity targetOrientation) {
		double angle = targetOrientation.angleInRadians
				* targetOrientation.deltaTime
				/ deltaTime;
		return new AngularVelocity(angleInRadians + angle, deltaTime);
	}

	@Override
	public String toString() {
		return degrees() + "deg/ms";
	}
}
