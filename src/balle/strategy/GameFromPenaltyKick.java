package balle.strategy;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.strategy.executor.movement.GoToObjectPFN;
import balle.strategy.planner.GoToBall;
import balle.world.Snapshot;

public class GameFromPenaltyKick extends Game {

	private static final Logger LOG = Logger
			.getLogger(GameFromPenaltyKick.class);

	public long timeToKick = 0;
	public long timeToGame = 0;

    public GameFromPenaltyKick(Strategy goToBallStrategy) {
        super(goToBallStrategy);
	}

    @FactoryMethod(designator = "Game (Penalty Kick)", parameterNames = {})
    public static GameFromPenaltyKick gameFromPenaltyKickFactory() {
        return new GameFromPenaltyKick(new GoToBall(new GoToObjectPFN(0)));
    }

	@Override
	public void onStep(Controller controller, Snapshot snapshot) {
		if (timeToKick == 0) {
			timeToKick = System.currentTimeMillis() + 1000;
		} else if ((timeToKick <= System.currentTimeMillis())
				&& (timeToGame == 0)) {
			LOG.info("Kicking penalty!");
			timeToGame = System.currentTimeMillis() + 2000;
			controller.penaltyKick();
		} else if ((timeToGame != 0)
				&& (timeToGame <= System.currentTimeMillis())) {
			super.onStep(controller, snapshot);
		}

	}

}
