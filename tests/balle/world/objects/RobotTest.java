package balle.world.objects;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import balle.misc.Globals;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Velocity;

public class RobotTest {

    /**
     * Given a robot and a ball that is located just in front of it, the robot
     * should be marked as in possession of the ball
     */
    @Test
    public void testPossessesBall() {
        Robot robot = new Robot(new Coord(0.5, 0.5), new Velocity(0, 0, 1),
                new Orientation(0, true));

        Ball ball = new Ball(new Coord(0.5 + robot.getHeight() / 2
                + Globals.BALL_RADIUS, 0.5), new Velocity(0, 0, 1));

        assertTrue(robot.possessesBall(ball));

    }

    /**
     * Given a robot and a ball that is located just in front of it, the robot
     * should be marked as in possession of the ball. Different from
     * testPosessesBall just in the orientation of the robot.
     */
    @Test
    public void testPossessesBallOtherWayAround() {
        Robot robot = new Robot(new Coord(0.5, 0.5), new Velocity(0, 0, 1),
                new Orientation(-Math.PI, true));

        Ball ball = new Ball(new Coord(0.5 - robot.getHeight() / 2
                - Globals.BALL_RADIUS, 0.5), new Velocity(0, 0, 1));

        assertTrue(robot.possessesBall(ball));

    }
}
