package balle.strategy;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.world.Orientation;
import balle.world.Snapshot;

public class GameFromPenaltyDefence extends Game {

	private static Logger LOG = Logger.getLogger(GameFromPenaltyDefence.class);

	private Snapshot firstSnapshot = null;
	String robotState = "Center";
	int rotateSpeed = 0;

	public GameFromPenaltyDefence() throws UnknownDesignatorException {
		super();
	}

	public boolean isStillInPenaltyDefence() {

		if (firstSnapshot == null)
			return true;

		double ball_x = firstSnapshot.getBall().getPosition().getX();
		double ball_y = firstSnapshot.getBall().getPosition().getY();

		double aball_x = getSnapshot().getBall().getPosition().getX();
		double aball_y = getSnapshot().getBall().getPosition().getY();

		double distance = Math.sqrt(Math.pow(aball_x - ball_x, 2)
				+ Math.pow(aball_y - ball_y, 2));

		if (distance > 1) {
			return false;
		}

		return true;
	}

	@Override
	public void step(Controller controller) {

		if ((firstSnapshot == null)
				&& (getSnapshot().getBall().getPosition() != null))
			firstSnapshot = getSnapshot();

		Orientation opponentAngle = getSnapshot().getOpponent()
				.getOrientation();
		double threshold = Math.toRadians(20);
		Boolean isLeftGoal = getSnapshot().getOwnGoal().isLeftGoal();

		if (getSnapshot().getOwnGoal().getMaxY() <= getSnapshot().getBalle()
				.getPosition().getY() + 0.22) {
			robotState = "Up";
		} else if (getSnapshot().getOwnGoal().getMinY() >= getSnapshot()
				.getBalle().getPosition().getY() - 0.18) {
			robotState = "Down";
		} else {
			robotState = "Center";
		}

		LOG.debug("robotState: " + robotState);

		if (isLeftGoal == true) {
			// opponent shooting left
			if (opponentAngle.atan2styleradians() > Math.PI - threshold)
				moveTo("Up", controller);
			else if (opponentAngle.atan2styleradians() < -Math.PI + threshold)
				moveTo("Down", controller);
			else
				moveTo("Center", controller);

		} else {
			// opponent shooting right
			if (opponentAngle.atan2styleradians() > threshold)
				moveTo("Up", controller);
			else if (opponentAngle.atan2styleradians() < -threshold)
				moveTo("Down", controller);
			else
				moveTo("Center", controller);
		}

	}

	private void moveTo(String moveTo, Controller controller) {

		rotateSpeed = 0;
		LOG.debug("moveTo: " + moveTo);

		if (robotState.equals("Center") && moveTo.equals("Up"))
			rotateSpeed = 200;
		if (robotState.equals("Center") && moveTo.equals("Down"))
			rotateSpeed = -200;
		if (robotState.equals("Up") && moveTo.equals("Center"))
			rotateSpeed = -200;
		if (robotState.equals("Up") && moveTo.equals("Down"))
			rotateSpeed = -100;
		if (robotState.equals("Down") && moveTo.equals("Center"))
			rotateSpeed = 200;
		if (robotState.equals("Down") && moveTo.equals("Up"))
			rotateSpeed = 200;

		controller.setWheelSpeeds(rotateSpeed, rotateSpeed);
	}

}