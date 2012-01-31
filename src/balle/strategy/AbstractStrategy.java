package balle.strategy;

import balle.controller.Controller;
import balle.world.AbstractWorld;
import balle.world.Snapshot;

public abstract class AbstractStrategy extends Thread {

    private Controller    controller;
    private AbstractWorld world;

    private Snapshot      snapshot;
    private Snapshot      prevSnapshot;

    public AbstractStrategy(Controller controller, AbstractWorld world) {
        super();
        this.controller = controller;
        this.world = world;
    }

    @Override
    public void run() {
        snapshot = null;
        while (true) {

            Snapshot newSnapshot = world.getSnapshot();

            if ((newSnapshot != null) && (!newSnapshot.equals(prevSnapshot))) {
                this.aiStep();
                prevSnapshot = snapshot;
                snapshot = newSnapshot;
            }
            this.aiMove(controller);
        }
    }

    protected AbstractWorld getWorld() {
        return world;
    }

    protected Snapshot getSnapshot() {
        return snapshot;
    }

    /**
     * This function is a step counter for the vision input. It is increased
     * every time a new snapshot of the world is received.
     */
    protected abstract void aiStep();

    /**
     * The code that implements movement strategy should go here
     * 
     * @param controller
     *            -- robot's controller to use
     */
    protected abstract void aiMove(Controller controller);

}
