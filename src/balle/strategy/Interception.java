package balle.strategy;

import java.awt.Color;

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
import balle.world.Predictor;
import balle.world.Snapshot;
import balle.world.objects.Ball;
import balle.world.objects.CircularBuffer;
import balle.world.objects.Goal;
import balle.world.objects.Pitch;
import balle.world.objects.Point;
import balle.world.objects.Robot;

public class Interception extends AbstractPlanner {

    private int     ballCount          = 0;
    private int     countNeeded        = 6;              // require 6 readings
                                                          // of ball position
    private boolean predictionCoordSet = false;
    private Coord   intercept          = new Coord(0, 0);
    private double  lineLength         = 200;
    protected CircularBuffer ballBuffer         = new CircularBuffer(6);

    private static Logger LOG                = Logger.getLogger(Interception.class);

    private MovementExecutor movementExecutor = new GoToObjectPFN(
            Globals.ROBOT_LENGTH / 2, false);

    protected void setIAmDoing(String message) {
        LOG.info(message);
    }

    public Interception() {
        super();
        // TODO Auto-generated constructor stub
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
        ballBuffer.addCoord(ball.getPosition());



        // run kicker to get ball moving - comment out when testing on pitch
        // executor.kick();

        if (ballIsMoving(ball) /* && !predictionCoordSet */) {
            // read a certain number of values
            ballCount += 1;
            if (ballCount > countNeeded) {
                lineLength = 4 * distanceOfMovingBall(ball);
                intercept = getPredictionCoord(snapshot.getPitch(), ball, lineLength, ballBuffer);
                predictionCoordSet = true;
            }
            // setIAmDoing("Getting ball positions");
            /*
             * } else if (robot.isInCoord(intercept)){ //reset to find new
             * prediction point predictionCoordSet = false; ballCount = 0;
             * executor.stop(); setIAmDoing("Predict point reached - stopping");
             */
        }
        if (predictionCoordSet) {
            movementExecutor.updateTarget(new Point(intercept));
            setIAmDoing("Going to point - predict");
            movementExecutor.step(controller, snapshot);
            addDrawable(new Dot(intercept, Color.PINK));
        }


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
        Coord oldBall = new Coord(ballBuffer.getXPosAt(ballBuffer.getLastPosition()),
                ballBuffer.getYPosAt(ballBuffer.getLastPosition()));
        if (oldBall.dist(ball.getPosition()) > 0.1) {
            return true;
        } else
            return false;
    }

    private double distanceOfMovingBall(Ball ball) {
        Coord oldBall = new Coord(ballBuffer.getXPosAt(ballBuffer.getLastPosition()),
                ballBuffer.getYPosAt(ballBuffer.getLastPosition()));

        return oldBall.dist(ball.getPosition());
    }

    /**
     * Gets a point that is LENGTH away from the ball based on it's previous
     * positions
     * 
     * @param ball
     * @param length
     * @return
     */
    protected Coord getPredictionCoord(Pitch pitch, Ball ball, double length, CircularBuffer ballBuffer) {

        Predictor predictor = new Predictor();

        for (int i = 0; i < ballBuffer.getBufferLength(); i++) {
            addDrawable(new Dot(ballBuffer.getCoordAt(i), new Color(255, 255, 255, 20)));
        }

        double[] parameters = new double[4];
        predictor.fitLine(parameters, ballBuffer.getXBuffer(), ballBuffer.getYBuffer(), null, null,
                ballBuffer.getBufferLength());

        // get the x offset value such that distance of will always equal 100
        double lineLength = length;
        double xOffset = (lineLength / Math.sqrt(1 + parameters[1] * parameters[1]));

        // changed the offset if the ball is travelling right
        if (Math.abs(ballBuffer.getXPosAt(ballBuffer.getCurrentPosition()))
                - Math.abs(ballBuffer.getXPosAt(ballBuffer.getLastPosition())) > 0) {
            xOffset = xOffset * -1;
        }

        // define coordinates of the line to draw
        double x1 =  ball.getPosition().getX();
        double y1 = (parameters[1] * ball.getPosition().getX() + parameters[0]);
        double x2 = ball.getPosition().getX() + xOffset;
        double y2 = (parameters[1] * (ball.getPosition().getX() + xOffset) + parameters[0]);

        // draw the line between (x1,y1) and (x2,y2)
        addDrawable(new DrawableLine(new Line(x1, y1, x2, y2), Color.CYAN));

        Coord predictCoord = new Coord(x2, y2);

        // check if the line is going out of the pitch
        while (!pitch.containsCoord(predictCoord)) {
            double x = predictCoord.getX();
            double y = predictCoord.getY();

            addDrawable(new Dot(new Coord(x, y), Color.GRAY));

            // use symmetry to get point in pitch
            if (x > pitch.getMaxX()) {
                predictCoord = new Coord((pitch.getMaxX() - Math.abs(x - pitch.getMaxX())), y);
            } else if (x < pitch.getMinX()) {
                predictCoord = new Coord(pitch.getMinX() + Math.abs(x - pitch.getMinX()), y);
            }
            if (y > pitch.getMaxY()) {
                predictCoord = new Coord(x, (pitch.getMaxY() - Math.abs(y - pitch.getMaxY())));
            } else if (y < pitch.getMinY()) {
                predictCoord = new Coord(x, (pitch.getMinY() + Math.abs(y - pitch.getMinY())));
            }
        }

        return predictCoord;
    }
}