package balle.strategy;

import balle.strategy.bezierNav.BezierNav;
import balle.strategy.bezierNav.CurveNav;
import balle.strategy.curve.FiniteDifferenceCHI;
import balle.strategy.executor.movement.GoToObject;
import balle.strategy.executor.movement.GoToObjectAvoidOpponentPurePF;
import balle.strategy.executor.movement.GoToObjectPFN;
import balle.strategy.executor.movement.GoToObjectPurePF;
import balle.strategy.executor.movement.OrientedMovementExecutor;
import balle.strategy.executor.turning.FaceAngle;
import balle.strategy.planner.AbstractPlanner;
import balle.strategy.planner.DefensiveStrategy;
import balle.strategy.planner.DribbleMilestone2;
import balle.strategy.planner.GoToBall;
import balle.strategy.planner.GoToFaceBall;
import balle.strategy.planner.KickFromWall;
import balle.strategy.planner.SimpleGoToBall;
import balle.strategy.planner.SimpleGoToBallFaceGoal;

public class StrategyFactory {
	public static String[] availableDesignators() {
		String[] designators = { "BezierNav", "CurveNav", "GoToBallPurePFOnly",
				"GoToObjectAvoidOpponentPurePF", "GoToBall", "GoToFaceBall",
				"GoToBallPFN",
				"Dribble", "Blocking", "PFNavigation", "DefensiveStrategy",
				"Game", "GameFromPenaltyKick", "GameFromPenaltyDefence",
				"BallNearWall" };
		return designators;
	}


	public static AbstractPlanner createClass(String designator)
			throws UnknownDesignatorException {
		if (designator.equals("BezierNav")) {
			return new SimpleGoToBallFaceGoal(new BezierNav(
					new FiniteDifferenceCHI()));
		} else if (designator.equals("CurveNav")) {
			return new SimpleGoToBallFaceGoal(
					(OrientedMovementExecutor) new CurveNav(
							new FiniteDifferenceCHI(),
					new GoToObjectPurePF()));
		} else if (designator.equals("GoToObjectAvoidOpponentPurePF")) {
			return new SimpleGoToBall(new GoToObjectAvoidOpponentPurePF());
		} else if (designator.equals("GoToBallPurePFOnly")) {
			return new SimpleGoToBall(new GoToObjectPurePF());
		} else if (designator.equals("GoToBall")) {
			return new GoToBall(new GoToObject(new FaceAngle()), true);
		} else if (designator.equals("GoToFaceBall")) {
			return new GoToFaceBall(new GoToObject(new FaceAngle()),
					new FaceAngle());
		} else if (designator.equals("GoToBallPFN")) {
			return new GoToBall(new GoToObjectPFN(0.15f), true);
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
