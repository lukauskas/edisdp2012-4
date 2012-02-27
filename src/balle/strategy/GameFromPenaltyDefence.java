package balle.strategy;

import balle.controller.Controller;
import balle.world.objects.Ball;
import balle.world.objects.Goal;
import balle.world.objects.Robot;

public class GameFromPenaltyDefence extends Game {

    public GameFromPenaltyDefence() throws UnknownDesignatorException {
        super();
    }

    /**
     * Checks if robot is is still defending the penalty;
     * 
     * @return
     */
    public boolean isStillInPenaltyDefence() {
        // TODO: implement
        // Probably worth checking the distance of the ball to the goal line
        // here
        return true;
    }

    @Override
    public void step(Controller controller) {

        if (isStillInPenaltyDefence()) {
            Robot ourRobot = getSnapshot().getBalle();
            Robot opponent = getSnapshot().getOpponent();
            Ball ball = getSnapshot().getBall();
            Goal ownGoal = getSnapshot().getOwnGoal();

            // TODO: Do Penalty defence here
            // Try using ourRobot.getFacingLineBothWays() and
            // opponent.getBallKickLine(ball)
            // And finding their intersection and then moving to that point.
            // (Youll have to manually
            // set wheel speeds to positive or negative values here.
        } else {
            super.step(controller);
        }
    }

}
