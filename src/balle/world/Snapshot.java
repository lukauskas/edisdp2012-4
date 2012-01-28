package balle.world;

/**
 * Immutable
 */
public class Snapshot {

    private final Robot       opponent;
    private final Robot       bot;
    private final FieldObject ball;

    private final long        timestamp;

    public Snapshot(Robot opponent, Robot balle, FieldObject ball,
            long timestamp) {
        super();
        this.opponent = opponent;
        this.bot = balle;
        this.ball = ball;
        this.timestamp = timestamp;
    }

    public Robot getOpponent() {
        return opponent;
    }

    public Robot getBalle() {
        return bot;
    }

    public FieldObject getBall() {
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
        if (this.getClass() != other.getClass())
            return false;
        Snapshot otherSnapshot = (Snapshot) other;

        if (this.getTimestamp() == otherSnapshot.getTimestamp())
            return true;
        else
            return false;
    }
}
