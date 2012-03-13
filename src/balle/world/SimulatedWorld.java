package balle.world;

import balle.simulator.SimulatorWorld;
import balle.world.objects.Pitch;

/**
 * Uses a simulator to account for latency in system.
 * 
 */
public class SimulatedWorld extends BasicWorld {

	/**
	 * The simulator for this simulated world.
	 */
	protected SimulatorWorld worldModel;

	/**
	 * Time that the update method was called.
	 */
	protected long updateTimestamp = -1;

	public SimulatedWorld(boolean balleIsBlue, boolean goalIsLeft, Pitch pitch) {
		super(balleIsBlue, goalIsLeft, pitch);
	}

	/**
	 * Make sure world model is updated too.
	 */
	@Override
	public void update(double yPosX, double yPosY, double yDeg, double bPosX,
			double bPosY, double bDeg, double ballPosX, double ballPosY,
			long timestamp) {

		// Update this.prev with update information.
		super.update(yPosX, yPosY, yDeg, bPosX, bPosY, bDeg, ballPosX,
				ballPosY, timestamp);

		// Record time.
		updateTimestamp = System.currentTimeMillis();

		// Update world model with snapshot information.
		worldModel.setWithSnapshot(prev, isBlue());

		// TODO roll simulator forward until updateTimestamp time.
	}

	/**
	 * Gets a snapshot of the predicted world state last time the world
	 * information was propagated.
	 */
	@Override
	public Snapshot getSnapshot() {
		return worldModel.getSnapshot(updateTimestamp, prev.getPitch(),
				prev.getOpponentsGoal(), prev.getOwnGoal());
	}



}
