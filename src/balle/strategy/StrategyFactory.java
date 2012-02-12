package balle.strategy;

import balle.controller.Controller;
import balle.world.AbstractWorld;

public class StrategyFactory {
    public static String[] availableDesignators() {

        String[] designators = { "DummyStrategy", "GoToBall", "Strategy_M2" };
        return designators;
    }

    public static AbstractStrategy createClass(String designator,
            Controller controller, AbstractWorld world)
            throws UnknownDesignatorException {

        if (designator == "DummyStrategy") {
            return new DummyStrategy(controller, world);
        } else if (designator == "GoToBall") {
            return new GoToBall(controller, world);
        } else if (designator == "Strategy_M2") {
            return new Strategy_M2(controller, world);
        } else
            throw new UnknownDesignatorException("Don't know strategy \""
                    + designator + "\"");
    }

}
