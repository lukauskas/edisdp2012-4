package balle.strategy.planner;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.world.AbstractWorld;
import balle.world.processing.AbstractWorldProcessor;

public abstract class AbstractPlanner extends AbstractWorldProcessor {

    private final Controller    controller;
    private final static Logger LOG = Logger.getLogger(AbstractPlanner.class);

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
        LOG.info("Planner stopped");
        controller.stop();
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public void start() {
        super.start();
        LOG.info("Planner started");
    }
}
