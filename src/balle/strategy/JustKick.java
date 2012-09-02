package balle.strategy;

import balle.controller.Controller;
import balle.strategy.planner.AbstractPlanner;
import balle.world.Snapshot;

public class JustKick extends AbstractPlanner {

    @Override
    protected void onStep(Controller controller, Snapshot snapshot) throws ConfusedException {
        controller.kick();
    }


    @FactoryMethod(designator = "Just kick", parameterNames = {})
    public static final JustKick factory() {
        return new JustKick();
    }

}
