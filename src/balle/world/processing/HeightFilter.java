package balle.world.processing;

import balle.world.Coord;

/**
 * Class for filtering input from world to account for height differences
 * between the detected position and the real position at ground level.
 * 
 * @author s0952880
 */
public class HeightFilter {

	/**
	 * Point directly below camera.
	 */
	private Coord worldCenter;

	/**
	 * Height of camera above worldCenter.
	 */
	private double cameraHeight;

	/**
	 * Constructor for HeightFilter.
	 * 
	 * @param worldCenter
	 *            Point directly below the camera.
	 * @param cameraHeight
	 *            The height of the camera above worldCenter.
	 */
	public HeightFilter(Coord worldCenter, double cameraHeight) {
		this.worldCenter = worldCenter;
		this.cameraHeight = cameraHeight;
	}

	public Coord filter(Coord in, double height) {
		Coord d = in.sub(worldCenter);

		double ratio = height / cameraHeight;
		d.mult(ratio);
		d.add(worldCenter);

		return d;
	}

}
