package balle.strategy.bezierNav;

import java.util.ArrayList;

import org.neuroph.core.NeuralNetwork;

import balle.controller.Controller;
import balle.main.drawable.Drawable;
import balle.world.Snapshot;

public class NeuralNetExecutor implements WheelSpeedExecutor {

	protected NeuralNetwork neural;


	public NeuralNetExecutor(NeuralNetwork neural) {
		this.neural = neural;
	}

	@Override
	public boolean isFinished(Snapshot snapshot) {
		return true;
	}

	@Override
	public boolean isPossible(Snapshot snapshot) {
		return true;
	}

	@Override
	public void stop(Controller controller) {
		controller.stop();
	}

	@Override
	public ArrayList<Drawable> getDrawables() {
		return new ArrayList<Drawable>();
	}

	@Override
	public void step(Controller controller, Snapshot snapshot) {
		int oLeft, oRight;

		oLeft = (int) neural.getOutput()[0];
		oRight = (int) neural.getOutput()[1];

		controller.setWheelSpeeds(oLeft, oRight);
	}

	@Override
	public void update(double desLeft, double desRight, double actLeft,
			double actRight) {
		neural.setInput(desLeft, actLeft, desRight, actRight);
		neural.calculate();
	}

}
