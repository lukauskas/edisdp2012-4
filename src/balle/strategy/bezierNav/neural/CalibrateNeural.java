package balle.strategy.bezierNav.neural;

import org.apache.log4j.Logger;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.MultiLayerPerceptron;

import balle.controller.Controller;
import balle.misc.Globals;
import balle.strategy.FactoryMethod;
import balle.strategy.planner.AbstractPlanner;
import balle.world.Snapshot;


public class CalibrateNeural extends AbstractPlanner {

	public static final Logger LOG = Logger.getLogger(CalibrateNeural.class);
	public static long lastCheck;
	protected MultiLayerPerceptron mlp;
	protected NeuralNetExecutor nne;

	protected TrainingSet ts;

	private final boolean PIVOT = false;

	@FactoryMethod(designator = "CalibrateNeural", parameterNames = {})
	public static CalibrateNeural calibrateNeuralFactory() {
		return new CalibrateNeural();
	}

	public CalibrateNeural() {
		mlp = new MultiLayerPerceptron(4, 5, 5, 2);
		mlp.initializeWeights(0.5);

		nne = new NeuralNetExecutor(mlp);
		nne.addlistener(this);

		ts = new TrainingSet();
	}

	int leftWheelSpeed = -900;
	int rightWheelSpeed = -900;
	int i;
	int space;

	@Override
	protected void onStep(Controller controller, Snapshot snapshot) {
		snapshot.getTimestamp();
		int leftWheelSpeed = 0;
		int rightWheelSpeed = 0;
		
		long timeThisCheck = System.currentTimeMillis();
		long timeOut = timeThisCheck - lastCheck;
		
		if ( timeOut > 100) {
			controller.setWheelSpeeds(leftWheelSpeed, rightWheelSpeed);
	
			lastCheck = timeThisCheck;
			if (leftWheelSpeed < Globals.MAXIMUM_MOTOR_SPEED -20) {
				switch(space) {
					case 1: leftWheelSpeed += 20; space = 2; break;
					case 2: leftWheelSpeed += 20; space = 3; break;
					case 3: leftWheelSpeed += 20; space = 4; break;
					case 4: rightWheelSpeed += 20; space = 1; break;
				}
				
			}
}
		
		

	}

	private double angularVelToWheelSpeed(double angVel) {
		return (angVel * Globals.ROBOT_TRACK_WIDTH) / (PIVOT ? 1 : 2);
	}


	protected void record(double desLeft, double desRight, double actLeft,
			double actRight, double fitness) {
		LOG.trace(desLeft + " " + desRight);

		// TODO
	}

}
