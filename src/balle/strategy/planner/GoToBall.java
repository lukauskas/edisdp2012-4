/**
 * 
 */
package balle.strategy.planner;

import java.awt.Color;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.main.Drawable;
import balle.strategy.executor.movement.MovementExecutor;
import balle.world.objects.Ball;

/**
 * @author s0909773
 * 
 */
public class GoToBall extends AbstractPlanner {

    private static final Logger LOG = Logger.getLogger(GoToBall.class);

    MovementExecutor            executorStrategy;

    /**
     * @param controller
     * @param world
     */
    public GoToBall(MovementExecutor movementExecutor) {
        executorStrategy = movementExecutor;

    }

    @Override
    public void step(Controller controller) {
        if (getSnapshot() == null)
            return;

        // Update the current state of executor strategy
        executorStrategy.updateState(getSnapshot());
        // Update the target's location in executorStrategy (e.g. if target
        // moved)
        Ball ball = getSnapshot().getBall();
        executorStrategy.updateTarget(ball);
        // Draw the target
        if (ball.getPosition() != null)
            addDrawable(new Drawable(Drawable.POINT, ball.getPosition().getX(),
                    ball.getPosition().getY(), Color.CYAN));

        // If it says it is not finished, tell it to do something for a step.
        if (!executorStrategy.isFinished()) {
            executorStrategy.step(controller);
        } else {
            // Tell the strategy to stop doing whatever it was doing
            executorStrategy.stop(controller);
            LOG.info("We're finished");
        }
    }

    @Override
    public void stop(Controller controller) {
        if (!executorStrategy.isFinished())
            executorStrategy.stop(controller);

    }
}
