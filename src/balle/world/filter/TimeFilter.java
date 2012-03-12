package balle.world.filter;

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
		return new Snapshot(s.getOpponent(), s.getBalle(), s.getBall(),
				s.getOpponentsGoal(), s.getOwnGoal(), s.getPitch(),
				s.getTimestamp() - delay);
	}
}
