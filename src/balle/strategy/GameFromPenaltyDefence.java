package balle.strategy;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.world.Orientation;
import balle.world.Snapshot;

public class GameFromPenaltyDefence extends Game {
<<<<<<< HEAD

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
				.getPosition().getY() + 0.2) {
			robotState = "Up";
		} else if (getSnapshot().getOwnGoal().getMinY() >= getSnapshot()
				.getBalle().getPosition().getY() - 0.2) {
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
			rotateSpeed = 500;
		if (robotState.equals("Center") && moveTo.equals("Down"))
			rotateSpeed = -700;
		if (robotState.equals("Up") && moveTo.equals("Center"))
			rotateSpeed = -700;
		if (robotState.equals("Up") && moveTo.equals("Down"))
			rotateSpeed = -500;
		if (robotState.equals("Down") && moveTo.equals("Center"))
			rotateSpeed = 500;
		if (robotState.equals("Down") && moveTo.equals("Up"))
			rotateSpeed = 700;

		controller.setWheelSpeeds(rotateSpeed, rotateSpeed);
	}

=======
    double lastknownxballpos;

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
        // Robot opponent = getSnapshot().getOpponent();
        // fix if boolean expression for when penalty saved or scored
        if (!ball.intersects(ownGoal.getGoalLine())
                && !(ball.getVelocity().getX() > 0 || ball.getVelocity().getY() > 0)) {
            return true;
        } else {

            return false;
        }
    }

    @Override
    public void step(Controller controller) {
        double ourypossition = 0, bcrosspointy = 0, y = 0, travely = 0;
        if (isStillInPenaltyDefence()) {
            Robot ourRobot = getSnapshot().getBalle();
            Robot opponent = getSnapshot().getOpponent();

            if ((ourRobot.getPosition() == null)
                    || (opponent.getPosition() == null))
                return;

            Ball ball = getSnapshot().getBall();
            Goal ownGoal = getSnapshot().getOwnGoal();

            ourypossition = ourRobot.getPosition().getY();

            bcrosspointy = ourypossition;
            if (ourRobot.getFacingLineBothWays().intersects(
                    opponent.getBallKickLine(ball))) {
                bcrosspointy = ourRobot.getFacingLineBothWays()
                        .getIntersect(opponent.getBallKickLine(ball)).getY();
            }
            y = ourRobot.getFacingLineBothWays()
                    .getIntersect(opponent.getBallKickLine(ball)).getY();

            travely = (bcrosspointy * 10 - ourypossition * 10);

            if (bcrosspointy >= ourRobot.getPosition().getY()
                    && bcrosspointy <= ourRobot.getPosition().getY() + 0.02) {
                controller.stop();
            } else {
                if (y > ownGoal.getMinY() && y < ownGoal.getMaxY()) {
                    // add if robot betwee y of goal
                    if (travely > 0) {

                        controller.forward(580);

                    } else {

                        controller.backward(580);

                    }
                } else {
                    controller.stop();
                }

            }

        } else {
            super.step(controller);
        }
    }
>>>>>>> 8bf1185b7fc7804c4390c18fb7cb916b9b343faa
}
