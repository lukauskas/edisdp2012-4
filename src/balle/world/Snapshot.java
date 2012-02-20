package balle.world;

/**
 * Immutable
 */
public class Snapshot {

    private final Robot opponent;
    private final Robot bot;
    private final Ball  ball;

    private final Goal  opponentsGoal;
    private final Goal  ownGoal;

    private final long  timestamp;

    public Snapshot(Robot opponent, Robot balle, Ball ball, Goal opponentsGoal, Goal ownGoal,
            long timestamp) {
        super();
        this.opponent = opponent;
        this.bot = balle;
        this.ball = ball;
        this.timestamp = timestamp;
        this.opponentsGoal = opponentsGoal;
        this.ownGoal = ownGoal;
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
