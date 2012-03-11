package balle.strategy;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.strategy.executor.movement.GoToObjectPFN;
import balle.strategy.executor.turning.IncFaceAngle;
import balle.strategy.executor.turning.RotateToOrientationExecutor;
import balle.strategy.planner.AbstractPlanner;
import balle.strategy.planner.BackingOffStrategy;
import balle.strategy.planner.DefensiveStrategy;
import balle.strategy.planner.GoToBallSafe;
import balle.strategy.planner.KickFromWall;
import balle.strategy.planner.KickToGoal;
import balle.world.Orientation;
import balle.world.Snapshot;
import balle.world.objects.Ball;
import balle.world.objects.Goal;
import balle.world.objects.Pitch;
import balle.world.objects.Robot;

public class Game extends AbstractPlanner {

    private static final Logger LOG = Logger.getLogger(Game.class);
    // Strategies that we will need make sure to updateState() for each of them
    // and stop() each of them
	protected final Strategy defensiveStrategy;
	protected final Strategy goToBallStrategy;
	protected final Strategy pickBallFromWallStrategy;
	protected final AbstractPlanner backingOffStrategy;
	protected final RotateToOrientationExecutor turningExecutor;
	protected final KickToGoal kickingStrategy;

    @FactoryMethod(designator = "Game")
    public static Game gameFactory() {
        return new Game();
    }

    public Game() {
        defensiveStrategy = new DefensiveStrategy(new GoToObjectPFN(0.1f));
        // TODO: implement a new strategy that inherits from GoToBall but always
        // approaches the ball from correct angle. (This can be done by always
        // pointing robot
        // to a location that is say 0.2 m before the ball in correct direction
        // and then, once the robot reaches it, pointing it to the ball itself
        // so it reaches it.
        goToBallStrategy = new GoToBallSafe(); // new GoToBall(new
                                             // GoToObjectPFN(0));
        pickBallFromWallStrategy = new KickFromWall(new GoToObjectPFN(0));
		backingOffStrategy = new BackingOffStrategy();
        turningExecutor = new IncFaceAngle();
        kickingStrategy = new KickToGoal();

    }

    @Override
    public void stop(Controller controller) {
        defensiveStrategy.stop(controller);
        goToBallStrategy.stop(controller);
    }

    @Override
    public void onStep(Controller controller, Snapshot snapshot) {

        Robot ourRobot = snapshot.getBalle();
        Robot opponent = snapshot.getOpponent();
        Ball ball = snapshot.getBall();
        Goal ownGoal = snapshot.getOwnGoal();
        Pitch pitch = snapshot.getPitch();

		LOG.info(ourRobot.getVelocity().abs());

        if ((ourRobot.getPosition() == null) || (ball.getPosition() == null))
            return;
        
		if (backingOffStrategy.couldRun(snapshot)) {
			backingOffStrategy.step(controller, snapshot);
			return;
		}

        Orientation targetOrientation = ball.getPosition()
                .sub(ourRobot.getPosition()).orientation();

        if (ourRobot.possessesBall(ball)) {
            // Kick if we are facing opponents goal
            if (!ourRobot.isFacingGoalHalf(ownGoal)) {
                kickingStrategy.step(controller, snapshot);
                addDrawables(kickingStrategy.getDrawables());
            } else {
                LOG.warn("We need to go around the ball");
                // TODO: go around the ball here
            }
        } else if ((opponent.possessesBall(ball))
                && (opponent.isFacingGoal(ownGoal))) {
            LOG.info("Defending");
            // Let defensiveStrategy deal with it!
			defensiveStrategy.step(controller, snapshot);
            addDrawables(defensiveStrategy.getDrawables());
        } else if (ball.isNearWall(pitch)) {
			pickBallFromWallStrategy.step(controller, snapshot);
            addDrawables(pickBallFromWallStrategy.getDrawables());
        } else if (ball.isNear(ourRobot)
                && (ourRobot.isApproachingTargetFromCorrectSide(ball,
						snapshot.getOpponentsGoal()))) {
            if (Math.abs(ourRobot.getAngleToTurn(targetOrientation)) > (Math.PI / 4)) {
                LOG.info("Ball is near our robot, turning to it");
                turningExecutor.setTargetOrientation(targetOrientation);
				turningExecutor.step(controller, snapshot);
            } else {
                // Go forward!
                controller.setWheelSpeeds(400, 400);
            }
        } else {
            // Approach ball
			goToBallStrategy.step(controller, snapshot);
            addDrawables(goToBallStrategy.getDrawables());

        }

    }
}
