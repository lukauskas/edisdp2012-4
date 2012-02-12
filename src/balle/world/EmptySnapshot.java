package balle.world;

/**
 * An abstraction for Snapshot that creates it without any robots, etc. -- Just
 * to reduce the number of null checks.
 */
public class EmptySnapshot extends Snapshot {

    public EmptySnapshot() {
        super(new Robot(null, null, null), new Robot(null, null, null),
                new FieldObject(null, null), System.currentTimeMillis());
    }

}
