package balle.strategy;

import balle.controller.Controller;
import balle.world.AbstractWorld;

public class StrategyFactory {

    public static AbstractStrategy createClass(String designator,
            Controller controller, AbstractWorld world) {
        if (designator == "DummyStrategy") {
            return new DummyStrategy(controller, world);
        }
        if (designator == "GoToBall") {
            return new GoToBall(controller, world);
        } else
            return null;
    }

}
