package balle.strategy;

import balle.controller.Controller;
import balle.strategy.planner.AbstractPlanner;
import balle.strategy.planner.GoToBall;
import balle.world.AbstractWorld;

public class StrategyFactory {
    public static String[] availableDesignators() {

        String[] designators = { "DummyStrategy", "GoToBall", "Dribble" };
        return designators;
    }

    public static AbstractPlanner createClass(String designator,
            Controller controller, AbstractWorld world)
            throws UnknownDesignatorException {

        if (designator.equals("DummyStrategy")) {
            return new DummyStrategy(controller, world);
        } else if (designator.equals("GoToBall")) {
            return new GoToBall(controller, world);
        } else if (designator.equals("Dribble")) {
            return new Dribble(controller, world);
        } else
            throw new UnknownDesignatorException("Don't know strategy \""
                    + designator + "\"");
    }

}
