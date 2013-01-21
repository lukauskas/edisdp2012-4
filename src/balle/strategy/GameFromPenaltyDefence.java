package balle.strategy;

import java.awt.Color;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.main.drawable.Dot;
import balle.misc.Globals;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Snapshot;
import balle.world.objects.Ball;
import balle.world.objects.Goal;
import balle.world.objects.Robot;

public class GameFromPenaltyDefence extends Game {

    private int SPEED;

    private static Logger LOG = Logger.getLogger(GameFromPenaltyDefence.class);

	private Snapshot firstSnapshot = null;
	String robotState = "Center";
	int rotateSpeed = 0;

    private enum MovementDirection {
        FORWARD, BACKWARD, NONE
    };

	private boolean finished = false;

    public GameFromPenaltyDefence(int speed) {
        super();
        SPEED = speed;
	}

    public GameFromPenaltyDefence() {
        this(200);
    }

	
    @FactoryMethod(designator = "Game (Penalty Defence)", parameterNames = {})
	public static GameFromPenaltyDefence gameFromPenaltyDefenceFactory()
	{
        return new GameFromPenaltyDefence();
	}

    // @FactoryMethod(designator = "Game (Penalty Defence SP)", parameterNames =
    // { "speed" })
    // public static GameFromPenaltyDefence gameFromPenaltyDefenceFactory(
    // double speed) {
    // return new GameFromPenaltyDefence((int) speed);
    // }

	public boolean isStillInPenaltyDefence(Snapshot snapshot) {

		Coord ball = snapshot.getBall().getPosition();
		if ((ball == null) && snapshot.getOwnGoal().isLeftGoal())
			ball = new Coord(0.5, 0.6); // assume that ball is on penalty spot,
										// if
									// we cannot see it.
		if ((ball == null) && !snapshot.getOwnGoal().isLeftGoal())
			ball = new Coord(1.8, 0.6); // assume that ball is on penalty spot,
										// if
									// we cannot see it.

		double minX = 0;
		double maxX = 0.75;
		if (!snapshot.getOwnGoal().isLeftGoal()) {
			maxX = snapshot.getPitch().getMaxX();
			minX = maxX - 0.75;
		}

		if (ball.isEstimated()
				|| (ball.getY() < snapshot.getOwnGoal().getMaxY())
				&& (ball.getY() > snapshot.getOwnGoal().getMinY())
				&& (ball.getX() > minX) && (ball.getX() < maxX)) {

			return true;
		}

		finished = true;
		return false;
	}

    public MovementDirection getMovementDirection(Snapshot snapshot) {
        Robot opponent = snapshot.getOpponent();
        Robot ourRobot = snapshot.getBalle();
        Goal ourGoal = snapshot.getOwnGoal();
        Ball ball = snapshot.getBall();

        if ((opponent.getPosition() == null)
                || (ourRobot.getPosition() == null))
            return MovementDirection.NONE;
        

        Coord intersectionPoint = null;
        if ((ball.getPosition() != null) && (!ball.getPosition().isEstimated()))
            intersectionPoint = opponent.getBallKickLine(ball).getIntersect(
                    ourRobot.getFacingLineBothWays());

        if (intersectionPoint == null)
            intersectionPoint = opponent.getFacingLine().getIntersect(
                    ourRobot.getFacingLineBothWays());


        if (intersectionPoint == null)
            return MovementDirection.NONE;

        double yCoord = intersectionPoint.getY();
        yCoord = Math.max(yCoord, ourGoal.getMinY() + Globals.ROBOT_LENGTH / 3);
        yCoord = Math.min(yCoord, ourGoal.getMaxY() - Globals.ROBOT_LENGTH / 3);

        Coord targetPoint = new Coord(ourRobot.getPosition().getX(), yCoord);
        addDrawable(new Dot(targetPoint, Color.WHITE));
        
        boolean isUpward;
        Orientation ourOrientation = ourRobot.getOrientation();
        if ((ourOrientation.radians() >= 0)
                && (ourOrientation.radians() < Math.PI))
                isUpward = true;
        else
            isUpward = false;

        // If we are already blocking the point stop
        if (ourRobot.containsCoord(targetPoint))
            return MovementDirection.NONE;

        double diff = (targetPoint.getY() - ourRobot.getPosition().getY());
        if (diff > 0)
            if (isUpward)
                return MovementDirection.FORWARD;
            else
                return MovementDirection.BACKWARD;
        else if (isUpward)
            return MovementDirection.BACKWARD;
        else
            return MovementDirection.FORWARD;
            
    }
	@Override
	public void onStep(Controller controller, Snapshot snapshot) {

		if (finished || !isStillInPenaltyDefence(snapshot)) {
			super.onStep(controller, snapshot);
			return;
		}

		if (snapshot.getBalle().getPosition() == null) {
			return;
		}

		if ((firstSnapshot == null)
				&& (snapshot.getBall().getPosition() != null)) {
			firstSnapshot = snapshot;
		}

        MovementDirection movementDirection = getMovementDirection(snapshot);
        if (movementDirection == MovementDirection.FORWARD)
            controller.setWheelSpeeds(SPEED, SPEED);
        else if (movementDirection == MovementDirection.BACKWARD)
            controller.setWheelSpeeds(-SPEED, -SPEED);
		else
            controller.stop();
	}
}