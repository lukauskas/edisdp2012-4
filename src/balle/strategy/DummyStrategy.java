package balle.strategy;

import balle.controller.Controller;
import balle.world.AbstractWorld;

/**
 * A Dummy strategy for the robot, the robot will go forward for 50 aiSteps()
 * and then go backward for the rest.
 * 
 * @author s0909773
 * 
 */
public class DummyStrategy extends AbstractStrategy {

    protected int stepNumber                = -1;
    protected int timesMoveCalledOnThisStep = 0;

    public DummyStrategy(Controller controller, AbstractWorld world) {
        super(controller, world);
    }

    @Override
    protected void aiStep() {
        stepNumber = (stepNumber + 1) % 100;
        timesMoveCalledOnThisStep = 0;
    }

    @Override
    protected void aiMove(Controller controller) {

        if (timesMoveCalledOnThisStep > 0)
            return;

        timesMoveCalledOnThisStep++;

        if (stepNumber == 0) {
            controller.stop();
            controller.forward(controller.getMaximumWheelSpeed());
        }

        else if (stepNumber == 50) {
            controller.stop();
            controller.backward(controller.getMaximumWheelSpeed());
        }

    }
    
    @Override
    public String toString() {
    	return "Dummy Strategy";
    }
}
