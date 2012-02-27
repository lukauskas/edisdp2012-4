package balle.strategy;

import balle.controller.Controller;
import balle.world.objects.Ball;
import balle.world.objects.Goal;
import balle.world.objects.Robot;

public class GameFromPenaltyDefence extends Game {
	double lastknowngoalposstionx;

	public GameFromPenaltyDefence() throws UnknownDesignatorException {
		super();
	}

	/**
	 * Checks if robot is is still defending the penalty;
	 * 
	 * @return
	 */
	public boolean isStillInPenaltyDefence() {
		Ball ball = getSnapshot().getBall();
		Goal ownGoal = getSnapshot().getOwnGoal();
		if (ball.intersects(ownGoal.getGoalLine())
				|| (ball.getPosition().getX() > lastknowngoalposstionx && ownGoal
						.isLeftGoal())
				|| (ball.getPosition().getX() < lastknowngoalposstionx && ownGoal
						.isRightGoal())) {
			return false;
		}
		return true;
	}

	@Override
	public void step(Controller controller) {

		if (isStillInPenaltyDefence()) {
			Robot ourRobot = getSnapshot().getBalle();
			Robot opponent = getSnapshot().getOpponent();
			Ball ball = getSnapshot().getBall();
			Goal ownGoal = getSnapshot().getOwnGoal();

			lastknowngoalposstionx = ball.getPosition().getX();

			double bcrosspointy = ourRobot.getFacingLineBothWays()
					.getIntersect(opponent.getBallKickLine(ball)).getY();

			double ourypossition = ourRobot.getPosition().getY();

			double travely = (bcrosspointy - ourypossition);
			if (bcrosspointy == ourRobot.getPosition().getY()) {
				controller.stop();
			} else {
				if (travely > 0) {
					controller.forward(580);
				} else {
					controller.backward(580);
				}
			}
		} else {
			controller.stop();
			super.step(controller);
		}
	}
}
