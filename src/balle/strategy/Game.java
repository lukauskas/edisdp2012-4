package balle.strategy;

import java.awt.Color;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.main.drawable.Circle;
import balle.main.drawable.Drawable;
import balle.main.drawable.Label;
import balle.misc.Globals;
import balle.simulator.SnapshotPredictor;
import balle.strategy.bezierNav.BezierNav;
import balle.strategy.curve.CustomCHI;
import balle.strategy.executor.movement.GoToObjectPFN;
import balle.strategy.executor.turning.IncFaceAngle;
import balle.strategy.executor.turning.RotateToOrientationExecutor;
import balle.strategy.pathFinding.SimplePathFinder;
import balle.strategy.planner.AbstractPlanner;
import balle.strategy.planner.BackingOffStrategy;
import balle.strategy.planner.DefensiveStrategy;
import balle.strategy.planner.GoToBallSafeProportional;
import balle.strategy.planner.InitialStrategy;
import balle.strategy.planner.KickFromWall;
import balle.strategy.planner.SimpleGoToBallFaceGoal;
import balle.world.Coord;
import balle.world.Line;
import balle.world.Snapshot;
import balle.world.objects.Ball;
import balle.world.objects.Goal;
import balle.world.objects.Pitch;
import balle.world.objects.RectangularObject;
import balle.world.objects.Robot;

public class Game extends AbstractPlanner {

    private static final Logger LOG = Logger.getLogger(Game.class);
	// Strategies that we will need make sure to call stop() for each of them
	protected final Strategy defensiveStrategy;
	protected final Strategy pickBallFromWallStrategy;
    protected final BackingOffStrategy backingOffStrategy;
	protected final RotateToOrientationExecutor turningExecutor;
    protected final Dribble_M4 kickingStrategy;
    protected final InitialStrategy initialStrategy;
	protected final Strategy goToBallPFN;
	protected final Strategy goToBallBezier;

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

    @FactoryMethod(designator = "Game", parameterNames = { "init" })
    public static Game gameFactoryTesting2(boolean init) {
        return new Game(init);
    }

    public Game() {
        defensiveStrategy = new DefensiveStrategy(new GoToObjectPFN(0.1f));
        pickBallFromWallStrategy = new KickFromWall(new GoToObjectPFN(0));
		backingOffStrategy = new BackingOffStrategy();
        turningExecutor = new IncFaceAngle();
        kickingStrategy = new Dribble_M4();
        initialStrategy = new InitialStrategy();
		goToBallPFN = new GoToBallSafeProportional();
		goToBallBezier = new SimpleGoToBallFaceGoal(new BezierNav(
                new SimplePathFinder(new CustomCHI())));
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
        pickBallFromWallStrategy.stop(controller);
        backingOffStrategy.stop(controller);
    }

    @Override
    public void onStep(Controller controller, Snapshot snapshot) {

        Robot ourRobot = snapshot.getBalle();
        Robot opponent = snapshot.getOpponent();
        Ball ball = snapshot.getBall();
        Goal ownGoal = snapshot.getOwnGoal();
		Goal opponentsGoal = snapshot.getOpponentsGoal();
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

        SnapshotPredictor sp = snapshot.getSnapshotPredictor();
        addDrawable(new Circle(ourRobot.getFrontSide().midpoint(), 0.07,
                Color.GREEN));
        addDrawable(new Circle(ourRobot.getFrontSide().midpoint(), 0.1,
                Color.RED));

		Strategy strategy = getStrategy(snapshot);
		setCurrentStrategy(strategy.getClass().getName());
		strategy.step(controller, snapshot);
		addDrawables(strategy.getDrawables());
    }

	private Strategy getStrategy(Snapshot snapshot) {
		Robot ourRobot = snapshot.getBalle();
		Robot opponent = snapshot.getOpponent();
		Ball ball = snapshot.getBall();
		Goal ownGoal = snapshot.getOwnGoal();
		Goal opponentsGoal = snapshot.getOpponentsGoal();
		Pitch pitch = snapshot.getPitch();

		if (ourRobot.getFrontSide().midpoint().dist(ball.getPosition()) < 0.1
				&& !ourRobot.isFacingGoalHalf(ownGoal)) {
			return kickingStrategy;
		}

		// Could the opponent be in the way? use pfn if so
		RectangularObject corridor = new Line(ourRobot.getPosition(),
				ball.getPosition()).widen(0.5);

		if (corridor.containsCoord(opponent.getPosition())) {
			return goToBallBezier;
		}

		if (!ourRobot.isApproachingTargetFromCorrectSide(ball, opponentsGoal)) {
			return goToBallPFN;
		}

		if (ourRobot.getPosition().dist(ball.getPosition()) > 1) {
			return goToBallPFN;
		}

		// Bezier can have trouble next to walls
		if (ourRobot.isNearWall(pitch)
				&& (!ball.isNearWall(pitch) || ourRobot.getPosition().dist(
						ball.getPosition()) > 0.5)) {
			return goToBallPFN;
		}


		return goToBallBezier;

	}
}
