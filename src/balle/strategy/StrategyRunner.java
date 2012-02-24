package balle.strategy;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.world.AbstractWorld;
import balle.world.processing.AbstractWorldProcessor;

public class StrategyRunner extends AbstractWorldProcessor {

    private final Controller    controller;
    private final static Logger LOG = Logger.getLogger(StrategyRunner.class);

    private Strategy            currentStrategy;

    public StrategyRunner(Controller controller, AbstractWorld world) {
        super(world);
        this.controller = controller;
        this.currentStrategy = null;
    }

    @Override
    protected void actionOnStep() {

    }

    @Override
    protected void actionOnChange() {
        if (currentStrategy != null) {
            currentStrategy.updateState(getSnapshot());
            currentStrategy.step(controller);
        }

    }

    /**
     * Stops the current running strategy
     */
    public void stopStrategy() {
        if (currentStrategy != null) {
            LOG.info("Stopping " + currentStrategy.getClass().getName());
            currentStrategy.stop(controller);
            currentStrategy = null;
        }
    }

    /**
     * Starts the strategy
     * 
     * @param strategy
     *            the strategy
     */
    public void startStrategy(Strategy strategy) {
        // Stop the old strategy
        if (currentStrategy != null)
            stopStrategy();
        // Start the new strategy
        currentStrategy = strategy;
        LOG.info("Started " + currentStrategy.getClass().getName());
    }

    @Override
    public void cancel() {
        super.cancel();
        stopStrategy();
        LOG.info("StrategyRunner canceled");
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public void start() {
        super.start();
        LOG.info("StrategyRunner initialised");
    }
}
