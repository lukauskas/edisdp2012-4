package balle.strategy;

import balle.brick.Controller;
import balle.world.AbstractWorld;

/**
 * A Dummy strategy for the robot, the robot will go forward for 50 aiSteps()
 * and then go backward for the rest.
 * 
 * @author s0909773
 * 
 */
public class DummyStrategy extends AbstractStrategy {

    protected int stepNumber = -1;

    public DummyStrategy(Controller controller, AbstractWorld world) {
        super(controller, world);
    }

    @Override
    protected void aiStep() {
        stepNumber = (stepNumber + 1) % 100;
    }

    @Override
    protected void aiMove(Controller controller) {
        if (stepNumber == 0)
            controller.forward(controller.getMaximumWheelSpeed());
        else if (stepNumber == 50)
            controller.backward(controller.getMaximumWheelSpeed());
    }
}