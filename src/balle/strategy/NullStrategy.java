package balle.strategy;

import balle.controller.Controller;
import balle.strategy.planner.AbstractPlanner;
import balle.world.Snapshot;

public class NullStrategy extends AbstractPlanner {
	
	@FactoryMethod(designator = "NullStrategy")
	public static NullStrategy gameFactory() {
		return new NullStrategy();
	}

	public NullStrategy() {

	}

	@Override
	public void onStep(Controller controller, Snapshot snapshot) {
		if (snapshot == null) {
			System.out.println("No Snapshot");
			return;
		}
	}

}
