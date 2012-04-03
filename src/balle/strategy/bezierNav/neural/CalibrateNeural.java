package balle.strategy.bezierNav.neural;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import org.apache.log4j.Logger;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;

import balle.controller.Controller;
import balle.misc.Globals;
import balle.strategy.FactoryMethod;
import balle.strategy.UserInputStrategy;
import balle.world.Snapshot;
import balle.world.objects.Robot;

public class CalibrateNeural extends UserInputStrategy {

	public static final Logger LOG = Logger.getLogger(CalibrateNeural.class);

	protected MultiLayerPerceptron mlp;
	protected NeuralNetExecutor nne;

	protected ArrayList<NeuralObj> data = new ArrayList<NeuralObj>();

	protected Robot lastRobot = null, currentRobot = null;

	@FactoryMethod(designator = "CalibrateNeural", parameterNames = {})
	public static CalibrateNeural calibrateNeuralFactory() {
		return new CalibrateNeural();
	}


	public CalibrateNeural() {
		mlp = new MultiLayerPerceptron(4, 2);
		mlp.setLearningRule(new BackPropagation());

		mlp.initializeWeights(0.5);

		nne = new NeuralNetExecutor(mlp);
		nne.addlistener(this);
	}

	@Override
	public void onStep(Controller controller, Snapshot snapshot) {
		if (lastRobot == null)
			lastRobot = snapshot.getBalle();
		currentRobot = snapshot.getBalle();

		double velLeft, velRight;
		velLeft = snapshot.getBalle().getLeftWheelSpeed();
		velRight = snapshot.getBalle().getRightWheelSpeed();

		nne.update((int) leftWheelPower, (int) rightWheelPower, velLeft,
				velRight);
		nne.step(controller, snapshot);

	}

	@Override
	public void stop(Controller controller) {
		super.stop(controller);

		try {
			Globals.neuralDataFile.writeArray(data);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ConcurrentModificationException e) {
			e.printStackTrace();
			controller.stop();
		}
	}

	protected double currLeft = 0, currRight = 0;
	protected int desLeftCmd = 0, desRightCmd = 0;
	protected int sentLeftCmd = 0, sentRightCmd = 0;

	protected void record(int gLeft, int gRight, double cLeft, double cRight,
			double left, double right) {
		double lastLeft = lastRobot.getLeftWheelSpeed(), lastRight = lastRobot
				.getRightWheelSpeed();

		// Log data
		data.add(new NeuralObj(currLeft, currRight, desLeftCmd, desRightCmd,
				sentLeftCmd, sentRightCmd, lastLeft, lastRight));

		// Update
		this.sentLeftCmd = (int) left;
		this.sentRightCmd = (int) right;
		this.currLeft = cLeft;
		this.currRight = cRight;
		this.desLeftCmd = gLeft;
		this.desRightCmd = gRight;
		this.lastRobot = currentRobot;

		LOG.trace("Data size " + data.size() + "Commands\n" + left + "\n"
				+ right + "\n" + cLeft + "\n" + cRight + "\n" + gLeft + "\n"
				+ gRight);
	}



	protected float getFitness(int desLeftCmd, int desRightCmd,
			double currLeft, double currRight, double oLeft, double oRight) {
		return 0;
	}

	// public static long lastCheck;
	// private final boolean PIVOT = false;

	// private double angularVelToWheelSpeed(double angVel) {
	// return (angVel * Globals.ROBOT_TRACK_WIDTH) / (PIVOT ? 1 : 2);
	// }

	// int leftWheelSpeed = -900;
	// int rightWheelSpeed = -900;
	// int i;
	// int space;

	// @Override
	// public void onStep(Controller controller, Snapshot snapshot) {
	// snapshot.getTimestamp();
	// int leftWheelSpeed = 0;
	// int rightWheelSpeed = 0;
	//
	// long timeThisCheck = System.currentTimeMillis();
	// long timeOut = timeThisCheck - lastCheck;
	//
	// if ( timeOut > 100) {
	// controller.setWheelSpeeds(leftWheelSpeed, rightWheelSpeed);
	//
	// lastCheck = timeThisCheck;
	// if (leftWheelSpeed < Globals.MAXIMUM_MOTOR_SPEED -20) {
	// switch(space) {
	// case 1: leftWheelSpeed += 20; space = 2; break;
	// case 2: leftWheelSpeed += 20; space = 3; break;
	// case 3: leftWheelSpeed += 20; space = 4; break;
	// case 4: rightWheelSpeed += 20; space = 1; break;
	// }
	//
	// }
	// }
	//
	// }
}
