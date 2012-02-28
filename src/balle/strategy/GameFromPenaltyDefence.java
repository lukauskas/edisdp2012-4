package balle.strategy;

import org.apache.log4j.Logger;

import balle.controller.Controller;

public class GameFromPenaltyDefence extends Game {

	private static Logger LOG = Logger.getLogger(GameFromPenaltyDefence.class);

	// private Snapshot firstSnapshot = null;
	// String robotState = "Center";
	// int rotateSpeed = 0;
	private boolean First = true;

	private boolean finished = false;

	public GameFromPenaltyDefence() throws UnknownDesignatorException {
		super();
	}

	public boolean isStillInPenaltyDefence() {

		if ((getSnapshot().getBall().getPosition().getY() < getSnapshot()
				.getOwnGoal().getMaxY())
				&& (getSnapshot().getBall().getPosition().getY() > getSnapshot()
						.getOwnGoal().getMinY())
				&& (getSnapshot().getBall().getPosition().getX() > 0)
				&& (getSnapshot().getBall().getPosition().getX() < 0.6)) {

			return true;
		}

		/*
		 * if (firstSnapshot == null) return true;
		 * 
		 * double ball_x = firstSnapshot.getBall().getPosition().getX(); double
		 * ball_y = firstSnapshot.getBall().getPosition().getY();
		 * 
		 * double aball_x = getSnapshot().getBall().getPosition().getX(); double
		 * aball_y = getSnapshot().getBall().getPosition().getY();
		 * 
		 * double distance = Math.sqrt(Math.pow(aball_x - ball_x, 2) +
		 * Math.pow(aball_y - ball_y, 2));
		 * 
		 * if (distance > 1) { return false; }
		 */

		finished = true;
		return false;
	}

	@Override
	public void step(Controller controller) {

		if (finished || !isStillInPenaltyDefence()) {
			super.step(controller);
			return;
		}

		double top = getSnapshot().getOwnGoal().getMaxY();
		double bottom = getSnapshot().getOwnGoal().getMinY();
		double robot = getSnapshot().getBalle().getPosition().getY();

		if (First) {
			if (Math.random() < 0.5) {
				controller.setWheelSpeeds(-400, -400);
			} else {
				controller.setWheelSpeeds(400, 400);
			}

			First = false;
		}

		if (robot > top - 0.1)
			controller.setWheelSpeeds(-400, -400);
		if (robot < bottom + 0.1)
			controller.setWheelSpeeds(400, 400);

		/*
		 * if ((firstSnapshot == null) && (getSnapshot().getBall().getPosition()
		 * != null)) firstSnapshot = getSnapshot();
		 * 
		 * Orientation opponentAngle = getSnapshot().getOpponent()
		 * .getOrientation(); double threshold = Math.toRadians(15); Boolean
		 * isLeftGoal = getSnapshot().getOwnGoal().isLeftGoal();
		 * 
		 * if (getSnapshot().getOwnGoal().getMaxY() <= getSnapshot().getBalle()
		 * .getPosition().getY() + 0.15) { robotState = "Up"; } else if
		 * (getSnapshot().getOwnGoal().getMinY() >= getSnapshot()
		 * .getBalle().getPosition().getY() - 0.15) { robotState = "Down"; }
		 * else { robotState = "Center"; }
		 * 
		 * LOG.debug("robotState: " + robotState);
		 * 
		 * if (isLeftGoal == true) { // opponent shooting left if
		 * (opponentAngle.atan2styleradians() > Math.PI - threshold)
		 * moveTo("Up", controller); else if (opponentAngle.atan2styleradians()
		 * < -Math.PI + threshold) moveTo("Down", controller); else
		 * moveTo("Center", controller);
		 * 
		 * } else { // opponent shooting right if
		 * (opponentAngle.atan2styleradians() > threshold) moveTo("Up",
		 * controller); else if (opponentAngle.atan2styleradians() < -threshold)
		 * moveTo("Down", controller); else moveTo("Center", controller); }
		 * 
		 * }
		 * 
		 * private void moveTo(String moveTo, Controller controller) {
		 * 
		 * rotateSpeed = 0; LOG.debug("moveTo: " + moveTo);
		 * 
		 * if (robotState.equals("Center") && moveTo.equals("Up")) rotateSpeed =
		 * 200; if (robotState.equals("Center") && moveTo.equals("Down"))
		 * rotateSpeed = -200; if (robotState.equals("Up") &&
		 * moveTo.equals("Center")) rotateSpeed = -200; if
		 * (robotState.equals("Up") && moveTo.equals("Down")) rotateSpeed =
		 * -100; if (robotState.equals("Down") && moveTo.equals("Center"))
		 * rotateSpeed = 200; if (robotState.equals("Down") &&
		 * moveTo.equals("Up")) rotateSpeed = 200;
		 * 
		 * 
		 * controller.setWheelSpeeds(rotateSpeed, rotateSpeed);
		 */
	}
}