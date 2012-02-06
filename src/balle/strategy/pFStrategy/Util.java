package balle.strategy.pFStrategy;

//some utility functions.
public class Util {
	// converts a Radians velocity vector to Degrees.
	public static VelocityVec convertVeltoDegree(VelocityVec vector) {

		return new VelocityVec(vector.getLeft() / (Math.PI) * 180, vector
				.getRight()
				/ (Math.PI) * 180);

	}

	public static double normalize(double angle) {
		while ((angle < 0) | (angle >= (2 * Math.PI))) {
			if (angle < 0)
				angle += 2 * Math.PI;
			if (angle >= 2 * Math.PI)
				angle -= 2 * Math.PI;
		}
		return angle;
	}

	public static double map2Pi(double angle) {
		double norm = normalize(angle);
		if (norm > Math.PI)
			norm -= Math.PI;
		// if(norm>Math.PI/2)
		// norm=Math.PI-norm;
		return norm;

	}
}
