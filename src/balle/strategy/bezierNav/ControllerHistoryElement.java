package balle.strategy.bezierNav;



public class ControllerHistoryElement {

	private final int powerLeft;
	private final int powerRight;

	// private Snapshot snapshot;
	private final long timestamp;

	public ControllerHistoryElement(int powerLeft, int powerRight,
			long timestamp) {
		this.powerLeft = powerLeft;
		this.powerRight = powerRight;
		// this.snapshot = snapshot;
		this.timestamp = timestamp;
	}

	public int getPowerLeft() {
		return powerLeft;
	}

	public int getPowerRight() {
		return powerRight;
	}

	// public Snapshot getSnapshot() {
	// return snapshot;
	// }

	public long getTimestamp() {
		return timestamp;
	}

}
