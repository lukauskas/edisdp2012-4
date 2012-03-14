package balle.strategy;

import java.awt.Color;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.main.drawable.Drawable;
import balle.main.drawable.Label;
import balle.misc.Globals;
import balle.strategy.executor.movement.GoToObjectPFN;
import balle.strategy.executor.turning.IncFaceAngle;
import balle.strategy.executor.turning.RotateToOrientationExecutor;
import balle.strategy.planner.AbstractPlanner;
import balle.strategy.planner.BackingOffStrategy;
import balle.strategy.planner.DefensiveStrategy;
import balle.strategy.planner.GoToBallSafeProportional;
import balle.strategy.planner.InitialStrategy;
import balle.strategy.planner.KickFromWall;
import balle.strategy.planner.KickToGoal;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Snapshot;
import balle.world.objects.Ball;
import balle.world.objects.Goal;
import balle.world.objects.Pitch;
import balle.world.objects.Robot;

public class Game extends AbstractPlanner {

    private static final Logger LOG = Logger.getLogger(Game.class);
	// Strategies that we will need make sure to call stop() for each of them
	protected final Strategy defensiveStrategy;
	protected final Strategy goToBallStrategy;
	protected final Strategy pickBallFromWallStrategy;
	protected final AbstractPlanner backingOffStrategy;
	protected final RotateToOrientationExecutor turningExecutor;
	protected final KickToGoal kickingStrategy;
    protected final InitialStrategy initialStrategy;

    protected boolean initial;

    private String currentStrategy = null;

    public String getCurrentStrategy() {
        return currentStrategy;
    }

    @Override
    public ArrayList<Drawable> getDrawables() {
        ArrayList<Drawable> drawables = super.getDrawables();
        if (currentStrategy != null)
            drawables.add(new Label(currentStrategy, new Coord(
                    Globals.PITCH_WIDTH - 0.5, Globals.PITCH_HEIGHT + 0.2),
                    Color.WHITE));
        return drawables;
    }

    public void setCurrentStrategy(String currentStrategy) {
        this.currentStrategy = currentStrategy;
    }

    @FactoryMethod(designator = "Game")
    public static Game gameFactory() {
        return new Game(true);
    }

    @FactoryMethod(designator = "GameTesting")
    public static Game gameFactoryTesting() {
        return new Game(false);
    }

    public Game() {
        defensiveStrategy = new DefensiveStrategy(new GoToObjectPFN(0.1f));
        goToBallStrategy = new GoToBallSafeProportional();
        pickBallFromWallStrategy = new KickFromWall(new GoToObjectPFN(0));
		backingOffStrategy = new BackingOffStrategy();
        turningExecutor = new IncFaceAngle();
        kickingStrategy = new KickToGoal();
        initialStrategy = new InitialStrategy();
        initial = false;
    }

    public boolean isInitial(Snapshot snapshot) {
        if (initial == false)
            return false;

        // Check if we have ball
        Ball ball = snapshot.getBall();
        if (ball.getPosition() == null)
            return initial; // Return whatever is set to initial if we do not
                            // see it
        
        Coord centerOfPitch = new Coord(Globals.PITCH_WIDTH / 2,
                Globals.PITCH_HEIGHT / 2);
        Robot ourRobot = snapshot.getBalle();
        // If we have the ball, turn off initial strategy
        if ((ourRobot.getPosition() != null) && (ourRobot.possessesBall(ball)))
        {
            LOG.info("We possess the ball. Turning off initial strategy");
            setInitial(false);
        }
        // else If ball has moved 5 cm, turn off initial strategy
        else if (ball.getPosition().dist(centerOfPitch) > 0.05) {
            LOG.info("Ball has moved. Turning off initial strategy");
            setInitial(false);
        }
        
        return initial;

    }

    public void setInitial(boolean initial) {
        this.initial = initial;
    }

    public Game(boolean startWithInitial) {
        this();
        initial = startWithInitial;
        LOG.info("Starting game strategy with initial strategy turned on");
    }
    @Override
    public void stop(Controller controller) {
        defensiveStrategy.stop(controller);
        goToBallStrategy.stop(controller);
        pickBallFromWallStrategy.stop(controller);
        backingOffStrategy.stop(controller);
    }

    @Override
    public void onStep(Controller controller, Snapshot snapshot) {

        Robot ourRobot = snapshot.getBalle();
        Robot opponent = snapshot.getOpponent();
        Ball ball = snapshot.getBall();
        Goal ownGoal = snapshot.getOwnGoal();
        Pitch pitch = snapshot.getPitch();

        if ((ourRobot.getPosition() == null) || (ball.getPosition() == null))
            return;
        
		if (backingOffStrategy.shouldStealStep(snapshot)) {
			backingOffStrategy.step(controller, snapshot);
			return;
		}

        if (isInitial(snapshot)) {
            setCurrentStrategy(initialStrategy.getClass().getName());
            initialStrategy.step(controller, snapshot);
            addDrawables(initialStrategy.getDrawables());
            return;
        }

        if (ourRobot.possessesBall(ball)) {
            // Kick if we are facing opponents goal
            if (!ourRobot.isFacingGoalHalf(ownGoal)) {
                setCurrentStrategy(kickingStrategy.getClass().getName());
                kickingStrategy.step(controller, snapshot);
                addDrawables(kickingStrategy.getDrawables());
            } else {
                LOG.warn("We need to go around the ball");
             // TODO: turn the robot slightly so we face away from our
                // own goal.
                // Implement a turning executor that would use
                // setWheelSpeeds to some arbitrary low
                // number (say -300,300 and 300,-300) to turn to correct
                // direction and use it here.
                // it has to be similar to FaceAngle executor but should not
                // use the controller.rotate()
                // command that is blocking.

                Coord r, b, g;
                r = ourRobot.getPosition();
                b = ball.getPosition();
                g = ownGoal.getPosition();

                if (r.angleBetween(g, b).atan2styleradians() < 0) {
                    // Clockwise.
                    Orientation orien = ourRobot
                            .findMaxRotationMaintaintingPossession(ball, true);
                    System.out.println(orien);
                    turningExecutor.setTargetOrientation(orien);
					turningExecutor.step(controller, snapshot);
                } else {
                    // Anti-Clockwise
                    Orientation orien = ourRobot
                            .findMaxRotationMaintaintingPossession(ball, false);
                    System.out.println(orien);
                    turningExecutor.setTargetOrientation(orien);
					turningExecutor.step(controller, snapshot);
                }

            }
        } else if ((opponent.possessesBall(ball))
                && (opponent.isFacingGoal(ownGoal))) {
            LOG.info("Defending");
            setCurrentStrategy(defensiveStrategy.getClass().getName());
            // Let defensiveStrategy deal with it!
			defensiveStrategy.step(controller, snapshot);
            addDrawables(defensiveStrategy.getDrawables());
        } else if (ball.isNearWall(pitch)) {
            setCurrentStrategy(pickBallFromWallStrategy.getClass().getName());
			pickBallFromWallStrategy.step(controller, snapshot);
            addDrawables(pickBallFromWallStrategy.getDrawables());
            // } else if (ball.isNear(ourRobot)
            // && (ourRobot.isApproachingTargetFromCorrectSide(ball,
            // snapshot.getOpponentsGoal()))) {
            // if (Math.abs(ourRobot.getAngleToTurn(targetOrientation)) >
            // (Math.PI / 4)) {
            // LOG.info("Ball is near our robot, turning to it");
            // setCurrentStrategy(turningExecutor.getClass().getName());
            // turningExecutor.setTargetOrientation(targetOrientation);
            // turningExecutor.step(controller, snapshot);
            // } else {
            // setCurrentStrategy(null);
            // // Go forward!
            // controller.setWheelSpeeds(400, 400);
            // }
        } else {
            // Approach ball
            setCurrentStrategy(goToBallStrategy.getClass().getName());
			goToBallStrategy.step(controller, snapshot);
            addDrawables(goToBallStrategy.getDrawables());

        }

    }
}
