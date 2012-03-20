package balle.strategy;

import java.awt.Color;
import java.util.Iterator;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.main.drawable.Dot;
import balle.main.drawable.DrawableLine;
import balle.misc.Globals;
import balle.strategy.executor.movement.GoToObjectPFN;
import balle.strategy.executor.movement.MovementExecutor;
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
    protected CircularBuffer<Coord> ballCoordBuffer;

    private static Logger LOG                = Logger.getLogger(Interception.class);

    private MovementExecutor movementExecutor = new GoToObjectPFN(
            Globals.ROBOT_LENGTH / 4);

    protected void setIAmDoing(String message) {
        LOG.info(message);
    }

    public Interception() {
        super();
        ballCoordBuffer = new CircularBuffer<Coord>(6);
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
            intercept = getPredictionCoordVelocityvector(snapshot);
            predictionCoordSet = true;
            // setIAmDoing("Getting ball positions");
            /*
             * } else if (robot.isInCoord(intercept)){ //reset to find new
             * prediction point predictionCoordSet = false; ballCount = 0;
             * executor.stop(); setIAmDoing("Predict point reached - stopping");
             */
        } else {
            controller.stop();
        }
        if (predictionCoordSet) {
            movementExecutor.updateTarget(new Point(intercept));
            setIAmDoing("Going to point - predict");
            movementExecutor.step(controller, snapshot);
            addDrawable(new Dot(intercept, Color.BLACK));
            //addDrawable(new Label("Go go go", new Coord(-0.05, -0.05),
            // Color.BLUE));
        } else {}
            //addDrawable(new Label("Not yet", new Coord(-0.05, -0.05), Color.RED));


    }

    @FactoryMethod(designator = "InterceptsM4")
    public static final Interception factory() {
        return new Interception();
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


    protected Coord getPredictionCoordVelocityvector(Snapshot s) {
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

        if (pivot == null)
            return CP;
        
        return CP;
        //addDrawable(new Dot(pivot, Color.RED));


//        Coord scaler = CP.sub(pivot);
//        Orientation theta = ball.getPosition().angleBetween(
//                ourRobot.getPosition(), CP);
//        Orientation theta2 = ball.getPosition().angleBetween(CP,
//                ourRobot.getPosition());
//
//        double minTheta = Math.min(theta.radians(), theta2.radians());
//
//        //addDrawable(new Label(String.format("Theta: %.2f", minTheta),
//        // new Coord(0, -0.15), Color.CYAN));
//
//        scaler = scaler.mult(Math.abs(minTheta)
//                / (Math.PI / 2));
//
//        // addDrawable(new DrawableVector(pivot, scaler, Color.BLACK));
//        Coord predictCoord = pivot.add(scaler);
//        //addDrawable(new DrawableLine(rotatedRobotBallLine, Color.PINK));
//        
//
//        // Coord predictCoord = rotatedRobotBallLine
//        // .getIntersect(ballDirectionLine);
//
//        // if (predictCoord == null) {
//        // // if the lines do not intersect jsut get a point thats 1m away from
//        // // the ball
//        //
//        // predictCoord = ball.getPosition().add(vec);
//        // }
//        //
//        // //addDrawable(new DrawableVector(ball.getPosition(), vec,
//        // Color.WHITE));
//        return predictCoord;
    }


}