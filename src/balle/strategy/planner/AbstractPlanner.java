package balle.strategy.planner;

import balle.controller.Controller;
import balle.world.AbstractWorld;
import balle.world.processing.AbstractWorldProcessor;

public abstract class AbstractPlanner extends AbstractWorldProcessor {

    private Controller controller;

    public AbstractPlanner(Controller controller, AbstractWorld world) {
        super(world);
        this.controller = controller;
    }

    @Override
    protected void actionOnStep() {
        aiStep();
    }

    @Override
    protected void actionOnChange() {
        aiMove(controller);
    }

    protected abstract void aiStep();

    protected abstract void aiMove(Controller controller);

    @Override
    public void cancel() {
        super.cancel();
        controller.stop();
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
