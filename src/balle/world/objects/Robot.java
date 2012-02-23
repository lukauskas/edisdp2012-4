package balle.world.objects;

import java.awt.geom.Line2D;

import balle.misc.Globals;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Velocity;

public class Robot extends RectangularObject {

    public Robot(Coord position, Velocity velocity, Orientation orientation) {
        super(position, velocity, orientation, Globals.ROBOT_WIDTH,
                Globals.ROBOT_LENGTH);
    }

    public static final double POSSESS_DISTANCE = 0.05;

    /**
     * Returns true if the robot is in possession of the ball. That is if the
     * ball is close enough to the kicker that it can kick it.
     * 
     * @param ball
     * @return true, if robot is in possession of the ball
     */
    public boolean possessesBall(Ball ball) {
        Coord possessVector = new Coord(this.getHeight() / 2.0, 0);
        possessVector.rotate(getOrientation());
        Coord possessPosition = getPosition().add(possessVector);

        double distance = ball.getPosition().dist(possessPosition);
        return distance <= POSSESS_DISTANCE + ball.getRadius();
    }

    /**
     * Checks if the robot can score from this position. That is if it is in
     * possession of the ball and facing the goal and other robot is is not
     * blocking the path to the goal.
     * 
     * @param ball
     * @param goal
     * @param otherRobot
     * @return true, if is in scoring position
     */
    public boolean isInScoringPosition(Ball ball, Goal goal, Robot otherRobot) {
        if (!possessesBall(ball))
            return false;

        double x1, y1, x2, y2, x3, y3, x4, y4;
        x1 = goal.getLeftPostCoord().getX();
        y1 = goal.getLeftPostCoord().getY();
        x2 = goal.getRightPostCoord().getX();
        y2 = goal.getRightPostCoord().getY();

        x3 = ball.getPosition().getX();
        y3 = ball.getPosition().getY();

        Coord target = new Coord(Globals.ROBOT_MAX_KICK_DISTANCE, 0);
        target.rotate(getOrientation());

        x4 = target.getX();
        y4 = target.getY();

        return Line2D.linesIntersect(x1, y1, x2, y2, x3, y3, x4, y4);
    }

}
