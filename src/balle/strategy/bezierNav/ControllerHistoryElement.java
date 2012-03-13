package balle.strategy.bezierNav;

import balle.world.Snapshot;


public class ControllerHistoryElement {

	private int powerLeft;
	private int powerRight;
	private Snapshot snapshot;

	public ControllerHistoryElement(int powerLeft, int powerRight,
			Snapshot snapshot) {
		this.powerLeft = powerLeft;
		this.powerRight = powerRight;
		this.snapshot = snapshot;
	}

	protected int getPowerLeft() {
		return powerLeft;
	}

	protected int getPowerRight() {
		return powerRight;
	}

	protected Snapshot getSnapshot() {
		return snapshot;
	}

}
