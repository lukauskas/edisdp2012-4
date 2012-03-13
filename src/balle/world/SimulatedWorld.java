package balle.world;

import balle.world.objects.Pitch;

/**
 * Uses a simulator to account for latency in system.
 * 
 */
public class SimulatedWorld extends BasicWorld {

	/**
	 * The simulator for this simulated world.
	 */
	protected SimulatedWorld worldModel;

	public SimulatedWorld(boolean balleIsBlue, boolean goalIsLeft, Pitch pitch) {
		super(balleIsBlue, goalIsLeft, pitch);
	}

	/**
	 * Gets a snapshot of the predicted world state last time the world
	 * information was propagated.
	 */
	@Override
	public Snapshot getSnapshot() {
		return prev;
	}



}
