package balle.strategy;

import balle.strategy.executor.movement.GoToObject;
import balle.strategy.executor.movement.GoToObjectPFN;
import balle.strategy.executor.turning.FaceAngle;
import balle.strategy.planner.AbstractPlanner;
import balle.strategy.planner.DribbleMilestone2;
import balle.strategy.planner.GoToBall;

public class StrategyFactory {
    public static String[] availableDesignators() {

        String[] designators = { "GoToBall", "GoToBallPFN", "Dribble",
                "Blocking", "PFNavigation", "DefensiveStrategy", "Game" };
        return designators;
    }

    public static AbstractPlanner createClass(String designator)
            throws UnknownDesignatorException {

        if (designator.equals("GoToBall")) {
            return new GoToBall(new GoToObject(new FaceAngle()));
        } else if (designator.equals("GoToBallPFN")) {
            return new GoToBall(new GoToObjectPFN(0.15f));
        } else if (designator.equals("DummyStrategy")) {
            return new DummyStrategy();
        } else if (designator.equals("Blocking")) {
            return new Blocking();
        } else if (designator.equals("Dribble")) {
            return new DribbleMilestone2();
        } else if (designator.equals("PFNavigation")) {
            return new PFNavigation();
        } else if (designator.equals("DefensiveStrategy")) {
            return new DefensiveStrategy();
        } else if (designator.equals("Game")) {
            return new Game();
        } else
            throw new UnknownDesignatorException("Don't know strategy \""
                    + designator + "\"");
    }
}
