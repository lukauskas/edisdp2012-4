package balle.world;

import balle.world.objects.Ball;
import balle.world.objects.Goal;
import balle.world.objects.Pitch;
import balle.world.objects.Robot;

/**
 * Immutable
 */
public class Snapshot {

	private final Robot opponent;
	private final Robot bot;
	private final Ball ball;

	private final Goal opponentsGoal;
	private final Goal ownGoal;

	private final Pitch pitch;
	private final long timestamp;

	public Snapshot(Robot opponent, Robot balle, Ball ball, Goal opponentsGoal,
			Goal ownGoal, Pitch pitch, long timestamp) {
		super();
		this.opponent = opponent;
		this.bot = balle;
		this.ball = ball;
		this.timestamp = timestamp;
		this.opponentsGoal = opponentsGoal;
		this.ownGoal = ownGoal;
		this.pitch = pitch;
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
