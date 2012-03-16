package balle.world.filter;

import balle.world.MutableSnapshot;
import balle.world.Snapshot;

/**
 * approximates the true timestamp of the snapshot (Should be when the picture
 * was recorded by the camera)
 * 
 * @author s0906575
 * 
 */
public class TimeFilter implements Filter {

	private long delay;

	public TimeFilter(long delay) {
		this.delay = delay;
	}

	public Snapshot filter(Snapshot s) {
		MutableSnapshot ms = s.unpack();
		ms.setTimestamp(s.getTimestamp() - delay);
		return ms.pack();
	}
}
