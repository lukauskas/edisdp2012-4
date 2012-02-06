package balle.strategy;

import balle.controller.Controller;
import balle.world.AbstractWorld;
import balle.world.processing.AbstractWorldProcessor;

public abstract class AbstractStrategy extends AbstractWorldProcessor {

    private Controller controller;

    public AbstractStrategy(Controller controller, AbstractWorld world) {
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

}
