package balle.world;

import balle.world.objects.Ball;
import balle.world.objects.Goal;
import balle.world.objects.Pitch;
import balle.world.objects.Robot;

public class MutableSnapshot {

	private final AbstractWorld world;

	private Robot opponent;
	private Robot bot;
	private Ball ball;

	private Goal opponentsGoal;
	private Goal ownGoal;

	private Pitch pitch;
	private long timestamp;

	public MutableSnapshot(AbstractWorld world, Robot opponent, Robot balle,
			Ball ball, Goal opponentsGoal, Goal ownGoal, Pitch pitch,
			long timestamp) {

		super();
		this.world = world;
		this.opponent = opponent;
		this.bot = balle;
		this.ball = ball;
		this.timestamp = timestamp;
		this.opponentsGoal = opponentsGoal;
		this.ownGoal = ownGoal;
		this.pitch = pitch;
	}

	public Snapshot estimateAt(long time) {
		return world.estimateAt(time);
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
		return new MutableSnapshot(world, getOpponent(), getBalle(), getBall(),
				getOpponentsGoal(), getOwnGoal(), getPitch(), getTimestamp());
	}

	public Snapshot pack() {
		return new Snapshot(world, getOpponent(), getBalle(), getBall(),
				getOpponentsGoal(), getOwnGoal(), getPitch(), getTimestamp());
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