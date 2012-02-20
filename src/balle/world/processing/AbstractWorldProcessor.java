package balle.world.processing;

import balle.world.AbstractWorld;
import balle.world.Goal;
import balle.world.Snapshot;

/**
 * A class that is supposed to be a base class for any class that needs to
 * process world snapshots.
 * 
 * It defines a basic interface one could use to do this.
 * 
 * @author s0909773
 * 
 */
public abstract class AbstractWorldProcessor extends Thread {

    private Snapshot            snapshot;
    private Snapshot            prevSnapshot;
    private final AbstractWorld world;
    private boolean             shouldStop           = false;

    private long                lastSnapshotReceived = 0;
    private double              fps                  = 0;

    // private Goal

    public double getFPS() {
        return fps;
    }

    public long getFPSAge() {
        return System.currentTimeMillis() - lastSnapshotReceived;
    }

    public AbstractWorldProcessor(AbstractWorld world) {
        super();
        this.world = world;
    }

    @Override
    public void start() {
        shouldStop = false;
        super.start();
    }

    @Override
    public final void run() {
        snapshot = null;
        while (!shouldStop) {
            Snapshot newSnapshot = world.getSnapshot();
            // James, Daniel:
            // Timestamps of the 2 snapshots is frequently the same.
            if ((newSnapshot != null) && (!newSnapshot.equals(prevSnapshot))) {
                actionOnChange();
                prevSnapshot = snapshot;
                snapshot = newSnapshot;

                if (prevSnapshot != null && snapshot.getTimestamp() != prevSnapshot.getTimestamp()) {
                    registerSnapshotReceived();

                }
            }
            actionOnStep();
        }
    }

    private final void registerSnapshotReceived() {
        long timedelta = System.currentTimeMillis() - lastSnapshotReceived;
        this.fps = (1000f / (double) timedelta);
        lastSnapshotReceived = System.currentTimeMillis();
    }

    /**
     * Return the latest snapshot
     * 
     * @return snapshot
     */
    protected final Snapshot getSnapshot() {
        return snapshot;
    }

    protected final Goal getOurGoal() {
        return world.getOurGoal();
    }

    protected final Goal getOpponentGoal() {
        return world.getOpponentGoal();
    }

    /**
     * This function is a step counter for the vision input. It is increased
     * every time a new snapshot of the world is received.
     */
    protected abstract void actionOnStep();

    /**
     * This function will run when a snapshot that is different from the
     * previous one was received.
     */
    protected abstract void actionOnChange();

    public void cancel() {
        shouldStop = true;
    }
}
