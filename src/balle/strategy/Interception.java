package balle.strategy;

import java.awt.Color;
import java.util.Iterator;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.main.drawable.Dot;
import balle.main.drawable.DrawableLine;
import balle.main.drawable.DrawableVector;
import balle.misc.Globals;
import balle.strategy.bezierNav.BezierNav;
import balle.strategy.curve.CustomCHI;
import balle.strategy.executor.movement.GoToObjectPFN;
import balle.strategy.executor.movement.MovementExecutor;
import balle.strategy.executor.movement.OrientedMovementExecutor;
import balle.strategy.pathfinding.SimplePathFinder;
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

    private int     ballCount          = 0;
    private int     countNeeded        = 6;              // require 6 readings
                                                          // of ball position
    private boolean predictionCoordSet = false;
    private Coord   intercept          = new Coord(0, 0);
    private double  lineLength         = 200;

    protected final boolean useCpOnly;
    protected CircularBuffer<Coord> ballCoordBuffer;

    private static Logger LOG                = Logger.getLogger(Interception.class);

    private MovementExecutor movementExecutor;
    private OrientedMovementExecutor orientedMovementExecutor;

    protected void setIAmDoing(String message) {
        LOG.info(message);
    }

    public Interception(boolean useCpOnly, MovementExecutor movementExecutor,
            OrientedMovementExecutor orientedMovementExecutor) {
        super();
        ballCoordBuffer = new CircularBuffer<Coord>(6);
        this.useCpOnly = useCpOnly;
        
        this.movementExecutor = movementExecutor;
        this.orientedMovementExecutor = orientedMovementExecutor;
    }


    @Override
    public void onStep(Controller controller, Snapshot snapshot) {

        Coord optimum = new Coord(0, 0);
        Goal goal = snapshot.getOwnGoal();
        Ball ball = snapshot.getBall();
        if (ball.getPosition() == null)
            return;

        Robot opponent = snapshot.getOpponent();
        Robot robot = snapshot.getBalle();

        ballCoordBuffer.add(ball.getPosition());

        // run kicker to get ball moving - comment out when testing on pitch
        // executor.kick();

        if (ballIsMoving(ball) /* && !predictionCoordSet */) {
            // read a certain number of values

            // ballCount += 1;
            // if (ballCount > countNeeded) {
            // lineLength = 4 * distanceOfMovingBall(ball);
            // intercept = getPredictionCoord(snapshot.getPitch(), ball,
            // lineLength, ballBuffer);
            // predictionCoordSet = true;
            // }
            intercept = getPredictionCoordVelocityvector(snapshot, useCpOnly);
            predictionCoordSet = true;
            // setIAmDoing("Getting ball positions");
            /*
             * } else if (robot.isInCoord(intercept)){ //reset to find new
             * prediction point predictionCoordSet = false; ballCount = 0;
             * executor.stop(); setIAmDoing("Predict point reached - stopping");
             */
        } else {
            if (!predictionCoordSet)
                controller.stop();
        }
        if (predictionCoordSet) {
            setIAmDoing("Going to point - predict");
            addDrawable(new Dot(intercept, Color.BLACK));
            if (movementExecutor != null) {
                movementExecutor.updateTarget(new Point(intercept));
                addDrawables(movementExecutor.getDrawables());
                movementExecutor.step(controller, snapshot);
            } else if (orientedMovementExecutor != null) {
                orientedMovementExecutor.updateTarget(new Point(intercept),
                        snapshot.getOpponentsGoal().getGoalLine().midpoint()
                                .sub(intercept).orientation());
                addDrawables(orientedMovementExecutor.getDrawables());
                orientedMovementExecutor.step(controller, snapshot);
            }

            //addDrawable(new Label("Go go go", new Coord(-0.05, -0.05),
            // Color.BLUE));
        } else {}
            //addDrawable(new Label("Not yet", new Coord(-0.05, -0.05), Color.RED));


    }

    @FactoryMethod(designator = "InterceptsM4-CP-PFN")
    public static final Interception factoryCPPFN() {
        return new Interception(true, new GoToObjectPFN(
                Globals.ROBOT_LENGTH / 3), null);
    }

    @FactoryMethod(designator = "InterceptsM4-NCP-PFN")
    public static final Interception factoryNCPPFN() {
        return new Interception(false, new GoToObjectPFN(
                Globals.ROBOT_LENGTH / 3), null);
    }

    @FactoryMethod(designator = "InterceptsM4-CP-PFNF")
    public static final Interception factoryCPPFNF() {
        return new Interception(true, new GoToObjectPFN(
                Globals.ROBOT_LENGTH / 3, false), null);
    }

    @FactoryMethod(designator = "InterceptsM4-NCP-PFNF")
    public static final Interception factoryNCPPFNF() {
        return new Interception(false, new GoToObjectPFN(
                Globals.ROBOT_LENGTH / 3, false), null);
    }

    @FactoryMethod(designator = "InterceptsM4-CP-BZR")
    public static final Interception factoryCPBZR() {
        return new Interception(true, null, new BezierNav(new SimplePathFinder(
                new CustomCHI())));
    }

    @FactoryMethod(designator = "InterceptsM4-NCP-BZR")
    public static final Interception factoryNCPBZR() {
        return new Interception(false, null, new BezierNav(
                new SimplePathFinder(
                new CustomCHI())));
    }

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


    protected Coord getPredictionCoordVelocityvector(Snapshot s, boolean useCPOnly) {
        Ball ball = s.getBall();
        Robot ourRobot = s.getBalle();

        Velocity vel = ball.getVelocity();
        Coord vec = new Coord(vel.getX(), vel.getY());
        vec = vec.mult(0.5 / vec.abs());
        
        Line ballRobotLine = new Line(ball.getPosition(),
                ourRobot.getPosition());
        // .extendBothDirections(Globals.PITCH_WIDTH);
        //addDrawable(new DrawableLine(ballRobotLine, Color.WHITE));

        Line ballDirectionLine = new Line(ball.getPosition(), ball
                .getPosition().add(vec));
        ballDirectionLine = ballDirectionLine.extend(Globals.PITCH_WIDTH);
        //addDrawable(new DrawableLine(ballDirectionLine, Color.WHITE));


        Line rotatedRobotBallLine = ballRobotLine.rotateAroundPoint(
                ballRobotLine.midpoint(), Orientation.rightAngle)
                .extendBothDirections(Globals.PITCH_WIDTH);
        //addDrawable(new DrawableLine(rotatedRobotBallLine, Color.PINK));

        Coord pivot = rotatedRobotBallLine.getIntersect(ballDirectionLine);
       
        Coord CP = ballDirectionLine.closestPoint(ourRobot.getPosition());
        addDrawable(new DrawableLine(ballDirectionLine, Color.WHITE));
        addDrawable(new Dot(CP, Color.WHITE));
        addDrawable(new DrawableLine(new Line(CP, ourRobot.getPosition()),
                Color.CYAN));
        // addDrawable(new DrawableLine(new Line(CP, ball.getPosition()),
        // Color.ORANGE));

        
        if (useCPOnly)
            return CP;
        
        if (pivot == null)
            return CP;
        
        //addDrawable(new Dot(pivot, Color.RED));

        Coord scaler = CP.sub(pivot);
         Orientation theta = ball.getPosition().angleBetween(
         ourRobot.getPosition(), CP);
         Orientation theta2 = ball.getPosition().angleBetween(CP,
         ourRobot.getPosition());
        
         double minTheta = Math.min(theta.radians(), theta2.radians());
        
         // addDrawable(new Label(String.format("Theta: %.2f", minTheta),
         // new Coord(0, -0.15), Color.CYAN));
        

        scaler = scaler.mult(Math.abs(minTheta) / (Math.PI / 2));
        
         Coord predictCoord = pivot.add(scaler);
        Line robotPredictLine = new Line(ourRobot.getPosition(), predictCoord);
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