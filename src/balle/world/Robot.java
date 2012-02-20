package balle.world;

import balle.misc.Globals;

public class Robot extends RectangularObject {

    public Robot(Coord position, Velocity velocity, Orientation orientation) {
        super(position, velocity, orientation, Globals.ROBOT_WIDTH, Globals.ROBOT_LENGTH);
    }

    /**
     * Returns true if the robot is in possession of the ball. That is if the
     * ball is close enough to the kicker that it can kick it.
     * 
     * @param ball
     * @return true, if robot is in possession of the ball
     */
    public boolean possessesBall(Ball ball) {
        // TODO: implement this.
        return false;
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
        // TODO: implement this
        return false;
    }

}
