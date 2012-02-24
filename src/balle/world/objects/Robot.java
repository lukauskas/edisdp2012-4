package balle.world.objects;

import java.awt.geom.Line2D;

import balle.misc.Globals;
import balle.world.Coord;
import balle.world.Line;
import balle.world.Orientation;
import balle.world.Velocity;

public class Robot extends RectangularObject {

    public Robot(Coord position, Velocity velocity, Orientation orientation) {
        super(position, velocity, orientation, Globals.ROBOT_WIDTH,
                Globals.ROBOT_LENGTH);
    }

    /**
     * Returns true if the robot is in possession of the ball. That is if the
     * ball is close enough to the kicker that it can kick it.
     * 
     * @param ball
     * @return true, if robot is in possession of the ball
     */
    public boolean possessesBall(Ball ball) {
        if (ball.getPosition() == null)
            return false;
        Coord possessVector = new Coord(this.getHeight() / 2.0, 0);
        possessVector = possessVector.rotate(getOrientation());

        Coord possessPosition = getPosition().add(possessVector);
        
        double distance = ball.getPosition().dist(possessPosition);
        return distance <= Globals.ROBOT_POSSESS_DISTANCE + ball.getRadius();
    }

    /**
     * Returns the line that would represents the path of the ball if the robot
     * kicked it
     * 
     * @param ball
     * @return
     */
    public Line getBallKickLine(Ball ball) {
        double x0, y0, x1, y1;
        x0 = ball.getPosition().getX();
        y0 = ball.getPosition().getY();

        Coord target = new Coord(Globals.ROBOT_MAX_KICK_DISTANCE, 0);
        target.rotate(getOrientation());

        x1 = target.getX();
        y1 = target.getY();

        return new Line(x0, y0, x1, y1);
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

        Line ballKickLine = getBallKickLine(ball);

        return Line2D.linesIntersect(x1, y1, x2, y2,
                ballKickLine.getA().getX(), ballKickLine.getA().getY(),
                ballKickLine.getB().getX(), ballKickLine.getB().getY());
    }

    /**
     * Returns true if robot is facing the goal. Similar to isInScoringPosition
     * but does not check whether a robot has a ball and whether it is blocked
     * by other robot.
     * 
     * @param goal
     * @return
     */
    public boolean isFacingGoal(Goal goal) {
        // TODO: finish
        return false;
    }

}
