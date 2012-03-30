package balle.world;

import java.util.ArrayList;
import java.util.List;

import balle.strategy.bezierNav.ControllerHistoryElement;
import balle.world.objects.Ball;
import balle.world.objects.Goal;
import balle.world.objects.Pitch;
import balle.world.objects.Robot;

public class MutableSnapshot {

	private BasicWorld world;
	
    private Robot             opponent;
	private Robot bot;
	private Ball ball;

	private List<ControllerHistoryElement> controllerHistory;

    private long                                timestamp;

	public void setControllerHistory(
			List<ControllerHistoryElement> controllerHistory) {
        this.controllerHistory = controllerHistory;
    }

	public List<ControllerHistoryElement> getControllerHistory() {
		return new ArrayList<ControllerHistoryElement>(controllerHistory);
    }

	public MutableSnapshot(BasicWorld world, Robot opponent, Robot balle,
			Ball ball, long timestamp,
			List<ControllerHistoryElement> controllerHistory) {
		
		this.world = world;
        this.controllerHistory = controllerHistory;
		this.opponent = opponent;
		this.bot = balle;
		this.ball = ball;
		this.timestamp = timestamp;
	}


	public Pitch getPitch() {
		return world.getPitch();
	}

	public void setWorld(BasicWorld w) {
		world = w;
	}

	public Goal getOpponentsGoal() {
		return world.getOpponentsGoal();
	}

	public Goal getOwnGoal() {
		return world.getOwnGoal();
	}

	public Robot getOpponent() {
		return opponent;
	}

	public void setOpponent(Robot r) {
		opponent = r;
	}

	public Robot getBalle() {
		return bot;
	}

	public void setBalle(Robot r) {
		bot = r;
	}

	public Ball getBall() {
		return ball;
	}

	public void setBall(Ball b) {
		ball = b;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long t) {
		timestamp = t;
	}

	public MutableSnapshot duplicate() {
		return new MutableSnapshot(world, getOpponent(), getBalle(), getBall(),
				getTimestamp(), getControllerHistory());
	}

	public Snapshot pack() {
		return new Snapshot(world, getOpponent(), getBalle(), getBall(),
				getTimestamp(), getControllerHistory());
	}

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof Snapshot))
			return false;
		Snapshot otherSnapshot = (Snapshot) other;

		if (this.getTimestamp() == otherSnapshot.getTimestamp())
			return true;
		else
			return false;
	}
}
