package balle.strategy.bezierNav;

import balle.strategy.executor.Executor;

public interface WheelSpeedExecutor extends Executor {

	public void update(double desLeft, double desRight, double actLeft,
			double actRight);

}
