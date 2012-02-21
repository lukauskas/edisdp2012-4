package balle.world.objects;

import balle.misc.Globals;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Velocity;

public class Robot extends RectangularObject {

    public Robot(Coord position, Velocity velocity, Orientation orientation) {
        super(position, velocity, orientation, Globals.ROBOT_WIDTH, Globals.ROBOT_LENGTH);
    }

    
    public static final double POSSESS_DISTANCE = 0.1;
    
    /**
     * Returns true if the robot is in possession of the ball. That is if the
     * ball is close enough to the kicker that it can kick it.
     * 
     * @param ball
     * @return true, if robot is in possession of the ball
     */
    public boolean possessesBall(Ball ball) {
        Coord possessPosition = new Coord(this.getHeight()/2, 0);
        possessPosition.rotate(getOrientation());
        
        Coord dBall = ball.getPosition().sub(possessPosition);
        return dBall.abs() < POSSESS_DISTANCE;
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
