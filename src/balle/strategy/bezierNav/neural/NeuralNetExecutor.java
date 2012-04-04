package balle.strategy.bezierNav.neural;

import java.util.ArrayList;

import org.neuroph.core.NeuralNetwork;

import balle.controller.Controller;
import balle.main.drawable.Drawable;
import balle.strategy.bezierNav.WheelSpeedExecutor;
import balle.world.Snapshot;

public class NeuralNetExecutor implements WheelSpeedExecutor {

	protected NeuralNetwork neural;

	protected CalibrateNeural listener;

	protected double gLeft = 0, gRight = 0;
	protected double cLeft = 0, cRight = 0;

	public NeuralNetExecutor(NeuralNetwork neural) {
		this.neural = neural;
	}

	public void addlistener(CalibrateNeural cn) {
		this.listener = cn;
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
		double oLeft, oRight;

		oLeft = neural.getOutput()[0];
		oRight = neural.getOutput()[1];

		controller.setWheelSpeeds(convert(oLeft), convert(oRight));
		listener.record(gLeft, gRight, cLeft, cRight, oLeft, oRight);
	}

	@Override
	public void update(double desLeft, double desRight, double actLeft,
			double actRight) {
		double convDesLeft, convDesRight, convActLeft, convActRight;
		convDesLeft = desLeft;
		convDesRight = desRight;
		convActLeft = actLeft;
		convActRight = actRight;
		
		neural.setInput(convDesLeft, convActLeft, convDesRight, convActRight);
		neural.calculate();

		gLeft = convDesLeft;
		gRight = convDesRight;
		cLeft = convActLeft;
		cRight = convActRight;
	}

	public static double convert(int d) {
		return ((double) d) / 900.0;
	}

	public static int convert(double d) {
		return (int) Math.floor(d * 900.0);
	}


}
