package balle.strategy;

import balle.controller.Controller;
import balle.strategy.planner.AbstractPlanner;
import balle.world.Snapshot;
/**
 * A Dummy strategy for the robot, the robot will go forward for 50 aiSteps()
 * and then go backward for the rest.
 * 
 * @author s0909773
 * 
 */
public class DummyStrategy extends AbstractPlanner {

    protected int stepNumber                = -1;
    protected long timer = System.currentTimeMillis();
    boolean goingForward = false;
    boolean done = false;
    long tt = 4000;

    @Override
    public void onStep(Controller controller, Snapshot snapshot) throws ConfusedException {
        long t = System.currentTimeMillis();
        long dt = t - timer;
        if (dt > 0) {
            // controller.backward(100);
            controller.setWheelSpeeds(-720, -720);
        } else {
            controller.stop();
        }
        //
        // if (dt > 10000) {
        // controller.stop();
        // if(goingForward)
        // controller.backward(controller.getMaximumWheelSpeed());
        // else
        // controller.forward(controller.getMaximumWheelSpeed());
        // goingForward = !goingForward;
        // }
    }

	@Override
	public String toString() {
		return "Dummy Strategy";
	}
}
