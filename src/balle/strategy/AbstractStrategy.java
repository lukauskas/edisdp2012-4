package balle.strategy;

import balle.brick.Controller;
import balle.world.AbstractWorld;
import balle.world.Snapshot;

public abstract class AbstractStrategy extends Thread {

    private Controller    controller;
    private AbstractWorld world;

    private Snapshot      snapshot;

    public AbstractStrategy(Controller controller, AbstractWorld world) {
        super();
        this.controller = controller;
        this.world = world;
    }

    @Override
    public void run() {
        while (true) {
            snapshot = world.getSnapshot();
            this.aiStep();
            this.aiMove(controller);
        }
    }

    protected AbstractWorld getWorld() {
        return world;
    }

    protected Snapshot getSnapshot() {
        return snapshot;
    }

    protected abstract void aiStep();

    protected abstract void aiMove(Controller controller);

}
