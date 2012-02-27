package balle.strategy.planner;

import balle.controller.Controller;
import balle.world.AbstractWorld;

/**
 * Capacity test for kick function. Kicks at every frame.
 * 
 * @author s0909773
 * 
 */
public class KickCapacityTest extends AbstractPlanner {

    public KickCapacityTest(Controller controller, AbstractWorld world) {
        super(controller, world);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void aiStep() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void aiMove(Controller controller) {
        controller.kick();
    }

}
