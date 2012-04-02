package balle.strategy.bezierNav;

import balle.strategy.executor.Executor;

public interface WheelSpeedExecutor extends Executor {

	public void update(int desLeft, int desRight, double actLeft,
			double actRight);

}
