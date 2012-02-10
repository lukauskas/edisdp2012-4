package balle.world.processing;

import balle.world.AbstractWorld;
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
    private boolean             shouldStop = false;

    private double              fps        = 0;

    public double getFPS() {
        return fps;
    }

    public AbstractWorldProcessor(AbstractWorld world) {
        super();
        this.world = world;
    }

    @Override
    public final void start() {
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

                if (prevSnapshot != null
                        && snapshot.getTimestamp() != prevSnapshot
                                .getTimestamp()) {
                    long dTime = snapshot.getTimestamp()
                            - prevSnapshot.getTimestamp();
                    fps = ((double) 1000) / ((double) dTime);
                }
            }
            actionOnStep();
        }
    }

    /**
     * Return the latest snapshot
     * 
     * @return snapshot
     */
    protected final Snapshot getSnapshot() {
        return snapshot;
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
