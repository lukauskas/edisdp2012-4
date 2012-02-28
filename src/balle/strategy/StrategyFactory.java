package balle.strategy;

import balle.strategy.executor.movement.GoToObject;
import balle.strategy.executor.movement.GoToObjectPFN;
import balle.strategy.executor.movement.GoToObjectPurePF;
import balle.strategy.executor.turning.FaceAngle;
import balle.strategy.planner.AbstractPlanner;
import balle.strategy.planner.DefensiveStrategy;
import balle.strategy.planner.DribbleMilestone2;
import balle.strategy.planner.GoToBall;
import balle.strategy.planner.GoToFaceBall;
import balle.strategy.planner.KickFromWall;
import balle.strategy.planner.SimpleGoToBall;

public class StrategyFactory {
	public static String[] availableDesignators() {

		String[] designators = { "GoToBallPFOnly", "GoToBall", "GoToFaceBall",
				"GoToBallPFN", "Dribble", "Blocking", "PFNavigation",
				"DefensiveStrategy", "Game", "GameFromPenaltyKick",
				"GameFromPenaltyDefence", "BallNearWall" };
		return designators;
	}

	public static AbstractPlanner createClass(String designator)
			throws UnknownDesignatorException {

		if (designator.equals("GoToBall")) {
			return new GoToBall(new GoToObject(new FaceAngle()), true);
		} else if (designator.equals("GoToFaceBall")) {
			return new GoToFaceBall(new GoToObject(new FaceAngle()),
					new FaceAngle());
		} else if (designator.equals("GoToBallPFN")) {
			return new GoToBall(new GoToObjectPFN(0.15f), true);
		} else if (designator.equals("GoToBallPFOnly")) {
			return new SimpleGoToBall(new GoToObjectPurePF());
		} else if (designator.equals("DummyStrategy")) {
			return new DummyStrategy();
		} else if (designator.equals("Blocking")) {
			return new Blocking();
		} else if (designator.equals("Dribble")) {
			return new DribbleMilestone2();
		} else if (designator.equals("PFNavigation")) {
			return new PFNavigation();
		} else if (designator.equals("BallNearWall")) {
			return new KickFromWall(new GoToObjectPFN(0.15f));
		} else if (designator.equals("DefensiveStrategy")) {
			return new DefensiveStrategy(new GoToObjectPFN(0.1f));
		} else if (designator.equals("Game")) {
			return new Game();
		} else if (designator.equals("GameFromPenaltyKick")) {
			return new GameFromPenaltyKick();
		} else if (designator.equals("GameFromPenaltyDefence")) {
			return new GameFromPenaltyDefence();
		} else
			throw new UnknownDesignatorException("Don't know strategy \""
					+ designator + "\"");
	}
}
