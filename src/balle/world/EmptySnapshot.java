package balle.world;

/**
 * An abstraction for Snapshot that creates it without any robots, etc. -- Just
 * to reduce the number of null checks.
 */
public class EmptySnapshot extends Snapshot {

    public EmptySnapshot(Goal opponentsGoal, Goal ownGoal) {
        super(new Robot(null, null, null), new Robot(null, null, null), new Ball(null, null),
                opponentsGoal, ownGoal, System.currentTimeMillis());
    }
}
