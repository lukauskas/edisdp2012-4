package balle.world;

import java.util.ArrayList;

import balle.simulator.SnapshotPredictor;
import balle.strategy.bezierNav.ControllerHistoryElement;
import balle.world.objects.Ball;
import balle.world.objects.Goal;
import balle.world.objects.Pitch;
import balle.world.objects.Robot;

/**
 * Immutable
 */
public class Snapshot {
	
    private final ArrayList<ControllerHistoryElement> controllerHistory;

    public ArrayList<ControllerHistoryElement> getControllerHistory() {
        // Clone so we do not break the immutability assumption
        return (ArrayList<ControllerHistoryElement>) controllerHistory.clone();
    }

    private final Robot opponent;
	private final Robot bot;
	private final Ball ball;

	private final Goal opponentsGoal;
	private final Goal ownGoal;

	private final Pitch pitch;
	private final long timestamp;

    public Snapshot(Robot opponent, Robot balle,
			Ball ball, Goal opponentsGoal, Goal ownGoal, Pitch pitch,
 long timestamp, ArrayList<ControllerHistoryElement> controllerHistory) {

		super();
		this.opponent = opponent;
		this.bot = balle;
		this.ball = ball;
		this.timestamp = timestamp;
		this.opponentsGoal = opponentsGoal;
		this.ownGoal = ownGoal;
		this.pitch = pitch;
        this.controllerHistory = controllerHistory;
	}

    public Snapshot(Robot opponent, Robot balle, Ball ball, Goal opponentsGoal, Goal ownGoal,
            Pitch pitch, long timestamp) {

        // Create a new snapshot predictor based on the information provided
        this(opponent, balle, ball, opponentsGoal, ownGoal, pitch, timestamp,
                new ArrayList<ControllerHistoryElement>());
                
    }
	
    public SnapshotPredictor getSnapshotPredictor() {
        // Always create new in order not to break the immutability
        return new SnapshotPredictor(getOpponent(), getBalle(), getBall(), getOpponentsGoal(),
                getOwnGoal(), getPitch(), getTimestamp(),
 getControllerHistory());

    }

    public Pitch getPitch() {
		return pitch;
	}

	public Goal getOpponentsGoal() {
		return opponentsGoal;
	}

	public Goal getOwnGoal() {
		return ownGoal;
	}

	public Robot getOpponent() {
		return opponent;
	}

	public Robot getBalle() {
		return bot;
	}

	public Ball getBall() {
		return ball;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public Snapshot duplicate() {
        return new Snapshot(getOpponent(), getBalle(), getBall(), getOpponentsGoal(), getOwnGoal(),
                getPitch(), getTimestamp(), getControllerHistory());
	}

	public MutableSnapshot unpack() {
        return new MutableSnapshot(getOpponent(), getBalle(), getBall(), getOpponentsGoal(),
                getOwnGoal(), getPitch(), getTimestamp(), getControllerHistory());
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
