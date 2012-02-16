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
    protected long timer = System.currentTimeMillis();
    boolean goingForward = false;
    boolean done = false;
    long tt = 4000;

    public DummyStrategy(Controller controller, AbstractWorld world) {
        super(controller, world);
    }

    @Override
    protected void aiStep() {
    }

    @Override
    protected void aiMove(Controller controller) {
    	if(System.currentTimeMillis()%tt < tt/2) {
    		controller.forward(720);
    	}
    	else {
    		controller.backward(720);
    	}
    }
    
    @Override
    public String toString() {
    	return "Dummy Strategy";
    }
}
