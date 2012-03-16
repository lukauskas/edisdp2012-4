package balle.world.filter;

import balle.misc.Globals;
import balle.world.Coord;
import balle.world.MutableSnapshot;
import balle.world.Snapshot;
import balle.world.objects.Robot;

/**
 * Class for filtering input from world to account for height differences
 * between the detected position and the real position at ground level.
 * 
 * @author s0952880
 */
public class HeightFilter implements Filter {

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

	@Override
	public Snapshot filter(Snapshot s) {
		MutableSnapshot ms = s.unpack();
		Robot nBalle = s.getBalle();
		if (nBalle.getPosition() != null && !nBalle.getPosition().isEstimated()) {
			ms.setBalle(new Robot(filter(s.getBalle().getPosition(),
					Globals.ROBOT_HEIGHT), s.getBalle().getVelocity(), s
					.getBalle().getAngularVelocity(), s
.getBalle()
					.getOrientation()));
		}

		Robot nOpp = s.getOpponent();
		if (nOpp.getPosition() != null && !nOpp.getPosition().isEstimated()) {
			ms.setOpponent(new Robot(filter(s.getOpponent().getPosition(),
					Globals.ROBOT_HEIGHT), s.getOpponent().getVelocity(), s
					.getOpponent().getAngularVelocity(), s
.getOpponent()
					.getOrientation()));
		}

		return ms.pack();
	}

	public Coord filter(Coord in, double height) {
		if (in == null)
			return null;

		Coord d = in.sub(worldCenter);

		double ratio = (cameraHeight - height) / cameraHeight;
		d = d.mult(ratio);
		d = d.add(worldCenter);

		return d;
	}

}
