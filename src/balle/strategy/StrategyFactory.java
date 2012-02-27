package balle.strategy;

import balle.controller.Controller;
import balle.strategy.executor.movement.GoToObject;
import balle.strategy.executor.movement.GoToObjectPFN;
import balle.strategy.executor.turning.FaceAngle;
import balle.strategy.planner.AbstractPlanner;
import balle.strategy.planner.DribbleMilestone2;
import balle.strategy.planner.GoToBall;
import balle.strategy.planner.KickFromWall;
import balle.world.AbstractWorld;

public class StrategyFactory {
	public static String[] availableDesignators() {

        String[] designators = { "GoToBall", "GoToBallPFN", "Dribble",
                "Blocking", "PFNavigation", "BallNearWall" };
        return designators;
    }

	public static AbstractPlanner createClass(String designator,
			Controller controller, AbstractWorld world)
			throws UnknownDesignatorException {

        if (designator.equals("GoToBall")) {
            return new GoToBall(controller, world, new GoToObject(
                    new FaceAngle()));
        } else if (designator.equals("GoToBallPFN")) {
            return new GoToBall(controller, world, new GoToObjectPFN(0.15f));
        } else if (designator.equals("DummyStrategy")) {
            return new DummyStrategy(controller, world);
        } else if (designator.equals("Blocking")) {
            return new Blocking(controller, world);
        } else if (designator.equals("Dribble")) {
            return new DribbleMilestone2(controller, world);
        } else if (designator.equals("PFNavigation")) {
            return new PFNavigation(controller, world);
		} else if (designator.equals("BallNearWall")) {
			return new KickFromWall(controller, world, new GoToObjectPFN(0.15f));
        } else
            throw new UnknownDesignatorException("Don't know strategy \""
                    + designator + "\"");
    }
}
