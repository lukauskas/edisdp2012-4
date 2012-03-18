package balle.world.filter;

import balle.world.Snapshot;

/**
 * Performs world smoothing.
 * 
 * @author s0952880
 */
public class SmoothingFilter implements Filter {
	@Override
	public Snapshot filter(Snapshot s) {
		return s;
	}
}
