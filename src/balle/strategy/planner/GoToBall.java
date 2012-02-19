/**
 * 
 */
package balle.strategy.planner;

import balle.controller.Controller;
import balle.strategy.executor.movement.MovementExecutor;
import balle.world.AbstractWorld;

/**
 * @author s0909773
 * 
 */
public class GoToBall extends AbstractPlanner {

    MovementExecutor executorStrategy;

    /**
     * @param controller
     * @param world
     */
    public GoToBall(Controller controller, AbstractWorld world,
            MovementExecutor movementExecutor) {
        super(controller, world);
        executorStrategy = movementExecutor;

    }

    @Override
    protected void aiStep() {
        // Do .. nothing?

    }

    @Override
    protected void aiMove(Controller controller) {
        if (getSnapshot() == null)
            return;

        // Update the current state of executor strategy
        executorStrategy.updateState(getSnapshot());
        // Update the target's location in executorStrategy (e.g. if target
        // moved)
        executorStrategy.updateTarget(getSnapshot().getBall());

        // If it says it is not finished, tell it to do something for a step.
        if (!executorStrategy.isFinished())
            executorStrategy.step(controller);
        else {
            // Tell the strategy to stop doing whatever it was doing
            executorStrategy.stop(controller);

            // Be happy
            System.out.println("Finished");
        }
    }
}
