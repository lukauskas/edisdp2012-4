package balle.world;

import java.util.ArrayList;

import balle.controller.ControllerListener;
import balle.simulator.SnapshotPredictor;
import balle.strategy.bezierNav.ControllerHistoryElement;
import balle.world.objects.Pitch;

/**
 * Uses a simulator to account for latency in system.
 * 
 */
public class SimulatedWorld extends BasicWorld implements ControllerListener {


	/**
	 * Our robots history
	 */
	protected ArrayList<ControllerHistoryElement> controllerHistory;


	/**
	 * Time that the update method was called
	 */
	protected long updateTimestamp = -1;

	/**
	 * Current time in the worldModel
	 */
	protected long simulatorTimestamp = -1;

	/**
	 * 
	 * @param worldModel
	 * @param balleIsBlue
	 * @param goalIsLeft
	 * @param pitch
	 */
    public SimulatedWorld(boolean balleIsBlue,
			boolean goalIsLeft, Pitch pitch) {

		super(balleIsBlue, goalIsLeft, pitch);
		controllerHistory = new ArrayList<ControllerHistoryElement>();
	}
	
    @Override
    protected void updateSnapshot(Snapshot nextSnapshot) {
        MutableSnapshot unpackedSnapshot = nextSnapshot.unpack();
        unpackedSnapshot
                .setControllerHistory((ArrayList<ControllerHistoryElement>) controllerHistory
                        .clone());

        Snapshot snapshotWithControllerHistory = unpackedSnapshot.pack();
        SnapshotPredictor sp = snapshotWithControllerHistory.getSnapshotPredictor();
        Snapshot adjustedSnapshot = sp.getSnapshotAfterTime(0);
        synchronized (this) {
            this.prev = adjustedSnapshot;
        }
	 }



	/**
	 * 
	 * @param che
	 */
	@Override
	public void commandSent(ControllerHistoryElement che) {
		controllerHistory.add(che);
	}
}
