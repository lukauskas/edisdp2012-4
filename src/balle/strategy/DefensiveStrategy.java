package balle.strategy;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.strategy.planner.AbstractPlanner;

/**
 * The Class DefensiveStrategy. Controls the robot so it gets in between
 * opponent and our own goal and stays there.
 */
public class DefensiveStrategy extends AbstractPlanner {

    private final static Logger LOG = Logger.getLogger(DefensiveStrategy.class);

    @Override
    public void step(Controller controller) {
        // TODO: implement
        LOG.error("DefensiveStrategy not implemented");

    }

    @Override
    public void stop(Controller controller) {
        LOG.error("Defensive strategy not implemented");

    }
}
