package balle.strategy.planner;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.misc.Globals;
import balle.world.Snapshot;
import balle.world.objects.Ball;
import balle.world.objects.Goal;
import balle.world.objects.Robot;

/**
 * Strategy that tries to kick the ball into opponents goal
 */
public class KickToGoal extends AbstractPlanner {
    private static Logger LOG = Logger.getLogger(KickToGoal.class);
    public KickToGoal() {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onStep(Controller controller, Snapshot snapshot) {
        Robot ourRobot = snapshot.getBalle();
        Robot opponent = snapshot.getOpponent();
        Ball ball = snapshot.getBall();
        Goal goal = snapshot.getOpponentsGoal();

        // Skip potential nullpointer exceptions
        if ((ourRobot == null) || (ball == null)) {
            return;
        }
        
        if (!ourRobot.isFacingGoalHalf(goal)) {
            LOG.info("Robot is not facing the correct goal, turning");
            // TODO: turn as much as possible while still maintaining possession
            // or back off and go around the ball
        } else {
            if ((ourRobot.isFacingGoal(goal) && (opponent == null))
                    || (ourRobot.isInScoringPosition(ball, goal, opponent))) {
                LOG.info("We can score, kicking");
                controller.kick();
                // Slowly move towards the ball if we missed it
                controller.setWheelSpeeds(200, 200);
            } else if (ourRobot.isFacingGoal(goal)) {
                LOG.info("We are facing goal, but opponent is blocking the shot");
                if (ourRobot.isFacingLeft())
                {
                    if (ourRobot.getPosition().getY() > Globals.PITCH_HEIGHT/2)
                    {
                        // If we're facing left and in upper half of the pitch, turn right
                        controller.setWheelSpeeds(700, 600);
                    }
                    else
                    {
                        // Turn left
                        controller.setWheelSpeeds(600, 700);
                    }
                }
                else
                {
                    if (ourRobot.getPosition().getY() > Globals.PITCH_HEIGHT/2)
                    {
                        // If we're facing right and in upper half of the pitch, turn left
                        controller.setWheelSpeeds(600, 700);
                    }
                    else
                    {
                        // Else
                        // Turn right
                        controller.setWheelSpeeds(700, 600);
                    }
                }
                return;
            } else {
                LOG.info("We're not facing goal");
                // TODO: Check if it is better to kick the ball from this
                // position
                // and hopefully bounce from the wall, or whether it is better
                // to turn towards the goal
            }
            LOG.warn("Kicking anyway");
            // TODO: remove
            controller.kick();
        }

    }

}
