package balle.strategy.planner;

import balle.controller.Controller;
import balle.strategy.executor.movement.MovementExecutor;
import balle.world.AbstractWorld;
import balle.world.Coord;
import balle.world.objects.Ball;

public class KickFromWall extends AbstractPlanner {
	
	MovementExecutor            movementStrategy;

	public KickFromWall(Controller controller, AbstractWorld world) {
		super(controller, world);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void aiStep() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void aiMove(Controller controller) {
		// TODO Auto-generated method stub
		
		if (getSnapshot() == null)
            return;
		
		movementStrategy.updateState(getSnapshot());
		
        //movementStrategy.updateTarget();
		
		
		
	}

}
