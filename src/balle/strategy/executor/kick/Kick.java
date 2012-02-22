package balle.strategy.executor.kick;

import balle.controller.Controller;
import balle.strategy.executor.Executor;
import balle.world.Snapshot;

public class Kick implements Executor {
	
	private long 				  startTime 			  = System.currentTimeMillis();
	Snapshot                      currentState            = null;

	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		long currectTime = System.currentTimeMillis(); 
		if (currectTime - startTime > 1000)
			return true;
		else 
			return false;
	}

	@Override
	public boolean isPossible() {
		// TODO Auto-generated method stub
		return ((currentState != null) && (currentState.getBalle() != null) && (currentState.getBall() != null));
	}

	@Override
	public void updateState(Snapshot snapshot) {
		// TODO Auto-generated method stub
		currentState = snapshot;		
	}

	@Override
	public void step(Controller controller) {
		// TODO Auto-generated method stub
	
		if (!isPossible())
            return;

        if (isFinished())
            stop(controller);
        else
        	controller.kick();
        
		
	}

	@Override
	public void stop(Controller controller) {
		// TODO Auto-generated method stub
		controller.stop();
		
	}

	
}
