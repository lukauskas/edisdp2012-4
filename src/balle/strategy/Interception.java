package balle.strategy;

import java.awt.Color;
import java.util.Iterator;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.main.drawable.Circle;
import balle.main.drawable.Dot;
import balle.main.drawable.DrawableLine;
import balle.main.drawable.DrawableVector;
import balle.main.drawable.Label;
import balle.misc.Globals;
import balle.strategy.executor.movement.MovementExecutor;
import balle.strategy.executor.movement.OrientedMovementExecutor;
import balle.strategy.planner.AbstractPlanner;
import balle.world.Coord;
import balle.world.Line;
import balle.world.Orientation;
import balle.world.Snapshot;
import balle.world.Velocity;
import balle.world.objects.Ball;
import balle.world.objects.CircularBuffer;
import balle.world.objects.Goal;
import balle.world.objects.Point;
import balle.world.objects.Robot;

public class Interception extends AbstractPlanner {
    private boolean ballHasMoved = false;
    private Coord   intercept          = new Coord(0, 0);
    private boolean shouldPlayGame;
    private static final double STRATEGY_STOP_DISTANCE = 0.3;
    private static final double GO_DIRECTLY_TO_BALL_DISTANCE = STRATEGY_STOP_DISTANCE * 1.75;

    protected final boolean useCpOnly;
	protected final boolean mirror;
    protected CircularBuffer<Coord> ballCoordBuffer;

    private static Logger LOG                = Logger.getLogger(Interception.class);

    private MovementExecutor movementExecutor;
    private OrientedMovementExecutor orientedMovementExecutor;
    private AbstractPlanner gameStrategy;

    private boolean startGameAfterwards;

    protected void setIAmDoing(String message) {
        LOG.info(message);
    }

    public Interception(boolean useCpOnly, MovementExecutor movementExecutor,
            OrientedMovementExecutor orientedMovementExecutor, boolean mirror,
            boolean startGameAfterwards) {
        super();
        ballCoordBuffer = new CircularBuffer<Coord>(6);
        this.useCpOnly = useCpOnly;
		this.mirror = mirror;
        
        this.movementExecutor = movementExecutor;
        this.orientedMovementExecutor = orientedMovementExecutor;
        shouldPlayGame = false;
        this.gameStrategy = new Game(false);
        this.startGameAfterwards = startGameAfterwards;

        // new Game(new SimpleGoToBallFaceGoal(new BezierNav(
        // new SimplePathFinder(new CustomCHI()))), false);
    }


    @Override
    public void onStep(Controller controller, Snapshot snapshot) {

        Coord optimum = new Coord(0, 0);
        Goal goal = snapshot.getOwnGoal();
        Ball ball = snapshot.getBall();
        if (ball.getPosition() == null)
            return;

        ballCoordBuffer.add(ball.getPosition());

        if (ballIsMoving(ball) /* && !predictionCoordSet */) {
			intercept = getPredictionCoordVelocityvector(snapshot, useCpOnly,
					mirror);
            ballHasMoved = true;
        }

        if (!shouldPlayGame) {
            addDrawable(new Circle(snapshot.getBalle().getPosition(),
                    STRATEGY_STOP_DISTANCE, Color.red));
            addDrawable(new Circle(snapshot.getBalle().getPosition(),
                    STRATEGY_STOP_DISTANCE * 1.75, Color.red));
        }
        if (snapshot.getBalle().getPosition()
                .dist(snapshot.getBall().getPosition()) < STRATEGY_STOP_DISTANCE) {
            if (startGameAfterwards)
                shouldPlayGame = true;
        }

        if (shouldPlayGame) {
            setIAmDoing("GAME!");
            gameStrategy.step(controller, snapshot);
            addDrawables(gameStrategy.getDrawables());
        } else if (ballHasMoved) {
            setIAmDoing("Going to point - predict");

            Coord goToCoord = intercept;
            if (snapshot.getBalle().getPosition()
                    .dist(snapshot.getBall().getPosition()) < GO_DIRECTLY_TO_BALL_DISTANCE) {
                goToCoord = snapshot.getBall().getPosition();
            }
            addDrawable(new Dot(goToCoord, Color.BLACK));
            addDrawable(new Dot(intercept, new Color(0, 0, 0, 100)));
            if (movementExecutor != null) {
                movementExecutor.updateTarget(new Point(goToCoord));
                addDrawables(movementExecutor.getDrawables());
                movementExecutor.step(controller, snapshot);
            } else if (orientedMovementExecutor != null) {
                orientedMovementExecutor.updateTarget(new Point(goToCoord),
                        snapshot.getOpponentsGoal().getGoalLine().midpoint()
                                .sub(intercept).orientation());
                addDrawables(orientedMovementExecutor.getDrawables());
                orientedMovementExecutor.step(controller, snapshot);
            }
        } else {
            setIAmDoing("Waiting");
            controller.stop();
        }
    }

    // @FactoryMethod(designator = "InterceptsM4-CP-PFN", parameterNames = {})
    // public static final Interception factoryCPPFN() {
    // return new Interception(true, new GoToObjectPFN(
    // Globals.ROBOT_LENGTH / 3), null, true, true);
    // }
    //
    // @FactoryMethod(designator = "InterceptsM4-NCP-PFN", parameterNames = {})
    // public static final Interception factoryNCPPFN() {
    // return new Interception(false, new GoToObjectPFN(
    // Globals.ROBOT_LENGTH / 3), null, true, true);
    // }
    //
    // @FactoryMethod(designator = "InterceptsM4-CP-PFNF", parameterNames = {})
    // public static final Interception factoryCPPFNF() {
    // return new Interception(true, new GoToObjectPFN(
    // Globals.ROBOT_LENGTH / 3, false), null, true, true);
    // }
    //
    // @FactoryMethod(designator = "InterceptsM4-CP-PFNF-NG", parameterNames =
    // {})
    // public static final Interception factoryCPPFNFNG() {
    // return new Interception(true, new GoToObjectPFN(
    // Globals.ROBOT_LENGTH / 3, false), null, true, false);
    // }
    //
    // @FactoryMethod(designator = "InterceptsM4-NCP-PFNF", parameterNames = {})
    // public static final Interception factoryNCPPFNF() {
    // return new Interception(false, new GoToObjectPFN(
    // Globals.ROBOT_LENGTH / 3, false), null, true, true);
    // }
    //
    // @FactoryMethod(designator = "InterceptsM4-CP-BZR", parameterNames = {})
    // public static final Interception factoryCPBZR() {
    // return new Interception(true, null, new BezierNav(new SimplePathFinder(
    // new CustomCHI())), true, true);
    // }
    //
    // @FactoryMethod(designator = "InterceptsM4-CP-BZR-NG", parameterNames =
    // {})
    // public static final Interception factoryCPBZRNG() {
    // return new Interception(true, null, new BezierNav(new SimplePathFinder(
    // new CustomCHI())), true, false);
    // }
    //
    // @FactoryMethod(designator = "InterceptsM4-NCP-BZR", parameterNames = {})
    // public static final Interception factoryNCPBZR() {
    // return new Interception(false, null, new BezierNav(
    // new SimplePathFinder(new CustomCHI())), true, true);
    // }

    /**
     * Checks when ball has moved since last reading
     * 
     * @return
     */
    private boolean ballIsMoving(Ball ball) {
        Iterator<Coord> it = ballCoordBuffer.iterator();
        Coord lastKnownLoc = null;
        while (it.hasNext())
            lastKnownLoc = it.next();
        
        if (lastKnownLoc == null)
            return false;
        
        if (lastKnownLoc.dist(ball.getPosition()) > 0.05) {
            return true;
        } else {
            return false;
        }
    }


	protected Coord getPredictionCoordVelocityvector(Snapshot s,
			boolean useCPOnly, boolean mirror) {
        Ball ball = s.getBall();
        Robot ourRobot = s.getBalle();

		Coord ballPos, currPos;
		ballPos = ball.getPosition();
		currPos = ourRobot.getPosition();

		double dist = (new Line(currPos, s.getOwnGoal().getPosition()))
				.length();
		if (mirror && dist > (Globals.PITCH_WIDTH / 2)) {

			// // Mirror X position.
			// double dX = currPos.getX() - s.getPitch().getPosition().getX();
			// currPos = new Coord(s.getPitch().getPosition().getX() - dX,
			// currPos.getY());

			addDrawable(new Label("length = " + dist, new Coord(0, 0),
					Color.ORANGE));

			return s.getOwnGoal().getPosition();

		}


        Velocity vel = ball.getVelocity();
        Coord vec = new Coord(vel.getX(), vel.getY());
        vec = vec.mult(0.5 / vec.abs());
        
		Line ballRobotLine = new Line(ballPos, currPos);
        // .extendBothDirections(Globals.PITCH_WIDTH);
        //addDrawable(new DrawableLine(ballRobotLine, Color.WHITE));

		Line ballDirectionLine = new Line(ballPos, currPos.add(vec));
        ballDirectionLine = ballDirectionLine.extend(Globals.PITCH_WIDTH);
        //addDrawable(new DrawableLine(ballDirectionLine, Color.WHITE));


        Line rotatedRobotBallLine = ballRobotLine.rotateAroundPoint(
                ballRobotLine.midpoint(), Orientation.rightAngle)
                .extendBothDirections(Globals.PITCH_WIDTH);
        //addDrawable(new DrawableLine(rotatedRobotBallLine, Color.PINK));

        Coord pivot = rotatedRobotBallLine.getIntersect(ballDirectionLine);
       
		Coord CP = ballDirectionLine.closestPoint(currPos);
        addDrawable(new DrawableLine(ballDirectionLine, Color.WHITE));
        addDrawable(new Dot(CP, Color.WHITE));
		addDrawable(new DrawableLine(new Line(CP, currPos),
                Color.CYAN));
        // addDrawable(new DrawableLine(new Line(CP, ball.getPosition()),
        // Color.ORANGE));

        
		if (useCPOnly) {
			// Coord earlier = CP.add(ball.getPosition().sub(CP).getUnitCoord()
			// .mult(0.4));
//			return earlier;
			return CP;
		}
        
        if (pivot == null)
            return CP;
        
        //addDrawable(new Dot(pivot, Color.RED));

        Coord scaler = CP.sub(pivot);
		Orientation theta = ballPos.angleBetween(currPos, CP);
		Orientation theta2 = ballPos.angleBetween(CP, currPos);
        
         double minTheta = Math.min(theta.radians(), theta2.radians());
        
         // addDrawable(new Label(String.format("Theta: %.2f", minTheta),
         // new Coord(0, -0.15), Color.CYAN));
        

        scaler = scaler.mult(Math.abs(minTheta) / (Math.PI / 2));
        
		Coord predictCoord = pivot.sub(scaler);
		Line robotPredictLine = new Line(currPos, predictCoord);
        // robotPredictLine = robotPredictLine.extend(0.5);
        addDrawable(new DrawableLine(robotPredictLine, Color.PINK));
        predictCoord = robotPredictLine.getB();

         // addDrawable(new DrawableLine(rotatedRobotBallLine, Color.PINK));
        
         // Coord predictCoord = rotatedRobotBallLine
         // .getIntersect(ballDirectionLine);
        
         // if (predictCoord == null) {
         // // if the lines do not intersect jsut get a point thats 1m away
        // from
         // // the ball
         //
         // predictCoord = ball.getPosition().add(vec);
         // }
         //
         // //addDrawable(new DrawableVector(ball.getPosition(), vec,
         // Color.WHITE));
        addDrawable(new DrawableVector(pivot, scaler, Color.BLACK));
        return predictCoord;
    }


}
