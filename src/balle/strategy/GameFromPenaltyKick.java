package balle.strategy;

import org.apache.log4j.Logger;

import balle.controller.Controller;

public class GameFromPenaltyKick extends Game {

	private static final Logger LOG = Logger
			.getLogger(GameFromPenaltyKick.class);

	public long timeToKick = 0;
	public long timeToGame = 0;

	public GameFromPenaltyKick() throws UnknownDesignatorException {
		super();
	}

	@Override
	public void step(Controller controller) {
		if (timeToKick == 0) {
			timeToKick = System.currentTimeMillis() + 1000;
		} else if ((timeToKick <= System.currentTimeMillis())
				&& (timeToGame == 0)) {
			LOG.info("Kicking penalty!");
			timeToGame = System.currentTimeMillis() + 2000;
			controller.penaltyKick();
		} else if ((timeToGame != 0)
				&& (timeToGame <= System.currentTimeMillis())) {
			super.step(controller);
		}

	}

}
