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

}
