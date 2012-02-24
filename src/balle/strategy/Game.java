package balle.strategy;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.strategy.planner.AbstractPlanner;
import balle.world.Snapshot;
import balle.world.objects.Ball;
import balle.world.objects.Goal;
import balle.world.objects.Pitch;
import balle.world.objects.Robot;

public class Game extends AbstractPlanner {

    private static final Logger LOG = Logger.getLogger(Game.class);
    // Strategies that we will need make sure to updateState() for each of them
    // and stop() each of them
    Strategy                    defensiveStrategy;
    Strategy                    goToBallStrategy;
    Strategy                    pickBallFromWallStrategy;

    public Game() throws UnknownDesignatorException {
        defensiveStrategy = StrategyFactory.createClass("DefensiveStrategy");
        goToBallStrategy = StrategyFactory.createClass("GoToBallPFN");
        // TODO: UPDATE THIS
        pickBallFromWallStrategy = StrategyFactory.createClass("DummyStrategy");

    }

    @Override
    public void stop(Controller controller) {
        defensiveStrategy.stop(controller);
        goToBallStrategy.stop(controller);
    }

    @Override
    public void updateState(Snapshot snapshot) {
        // Update the state for ourselves
        super.updateState(snapshot);
        // Propagate the state to our strategies
        defensiveStrategy.updateState(snapshot);
        goToBallStrategy.updateState(snapshot);
    }

    @Override
    public void step(Controller controller) {

        Snapshot snapshot = getSnapshot();
        Robot ourRobot = snapshot.getBalle();
        Robot opponent = snapshot.getOpponent();
        Ball ball = snapshot.getBall();
        Goal ownGoal = snapshot.getOwnGoal();
        Goal opponentsGoal = snapshot.getOpponentsGoal();
        Pitch pitch = snapshot.getPitch();

        if (ourRobot.possessesBall(ball)) {
            // Kick if we are facing opponents goal
            if (ourRobot.isFacingGoal(opponentsGoal)) {
                LOG.info("Kicking the ball");
                controller.kick();
            } else {
                // TODO: turn towards opponent's goal?? or maybe go around ball?
                LOG.error("Unimplemented turn towards opponent's goal!");
            }
        } else if ((opponent.possessesBall(ball))
                && (opponent.isFacingGoal(ownGoal))) {
            LOG.info("Defending");
            // Let defensiveStrategy deal with it!
            defensiveStrategy.step(controller);
        } else if (!ball.isNearWall(pitch)) {
            LOG.info("Approaching ball");
            // Approach ball
            goToBallStrategy.step(controller);
            addDrawables(goToBallStrategy.getDrawables());
        } else if (ball.isNearWall(pitch)) {
            // TODO: Pick it
            // pickBallFromWallStrategy.step(controller);
            LOG.error("Pick ball from wall not implemented");
        }

    }
}
