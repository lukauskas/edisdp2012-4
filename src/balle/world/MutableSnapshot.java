package balle.world;

import java.util.ArrayList;

import balle.strategy.bezierNav.ControllerHistoryElement;
import balle.world.objects.Ball;
import balle.world.objects.Goal;
import balle.world.objects.Pitch;
import balle.world.objects.Robot;

public class MutableSnapshot {


    private Robot             opponent;
	private Robot bot;
	private Ball ball;

	private Goal opponentsGoal;
	private Goal ownGoal;

	private Pitch pitch;

	private final BallEstimator ballEstimator;

    private ArrayList<ControllerHistoryElement> controllerHistory;

    private long                                timestamp;

    public void setControllerHistory(ArrayList<ControllerHistoryElement> controllerHistory) {
        this.controllerHistory = controllerHistory;
    }

    public ArrayList<ControllerHistoryElement> getControllerHistory() {
        return (ArrayList<ControllerHistoryElement>) controllerHistory.clone();
    }

	public MutableSnapshot(Robot opponent, Robot balle, Ball ball,
			Goal opponentsGoal, Goal ownGoal, Pitch pitch,
			BallEstimator ballEstimator, long timestamp,
            ArrayList<ControllerHistoryElement> controllerHistory) {

		super();
        this.controllerHistory = controllerHistory;
		this.opponent = opponent;
		this.bot = balle;
		this.ball = ball;
		this.timestamp = timestamp;
		this.opponentsGoal = opponentsGoal;
		this.ownGoal = ownGoal;
		this.pitch = pitch;
		this.ballEstimator = ballEstimator;
	}


	public Pitch getPitch() {
		return pitch;
	}

	public void setPitch(Pitch p) {
		pitch = p;
	}

	public Goal getOpponentsGoal() {
		return opponentsGoal;
	}

	public void setOpponentsGoal(Goal g) {
		opponentsGoal = g;
	}

	public Goal getOwnGoal() {
		return ownGoal;
	}

	public void setOwnGoal(Goal g) {
		ownGoal = g;
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
		return new MutableSnapshot(getOpponent(), getBalle(), getBall(),
				getOpponentsGoal(), getOwnGoal(), getPitch(), ballEstimator,
				getTimestamp(), getControllerHistory());
	}

	public Snapshot pack() {
		return new Snapshot(getOpponent(), getBalle(), getBall(),
				getOpponentsGoal(), getOwnGoal(), getPitch(), ballEstimator,
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
