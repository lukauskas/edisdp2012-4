package balle.strategy.bezierNav.neural;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import org.apache.log4j.Logger;
import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingSet;
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
	protected BackPropagation bp;
	protected TrainingSet<SupervisedTrainingElement> ts;

	protected NeuralNetExecutor nne;

	protected ArrayList<NeuralObj> data = new ArrayList<NeuralObj>();

	protected Robot lastRobot = null, currentRobot = null;

	@FactoryMethod(designator = "CalibrateNeural", parameterNames = {})
	public static CalibrateNeural calibrateNeuralFactory() {
		return new CalibrateNeural();
	}


	public CalibrateNeural() {
		ts = new TrainingSet<SupervisedTrainingElement>();
		mlp = new MultiLayerPerceptron(4, 5, 2);

		bp = new BackPropagation();
		bp.setNeuralNetwork(mlp);
		bp.setLearningRate(1.0);

		mlp.randomizeWeights();

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

		// LOG.trace(velLeft + " " + velRight);
		LOG.trace(leftWheelPower + " " + rightWheelPower);

		// controller.setWheelSpeeds(900, 900);
		nne.update(leftWheelPower, rightWheelPower, velLeft,
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
	protected double desLeftCmd = 0, desRightCmd = 0;
	protected double sentLeftCmd = 0, sentRightCmd = 0;

	protected void record(double gLeft, double gRight, double cLeft,
			double cRight, double left, double right) {
		double actLeft = currentRobot.getLeftWheelSpeed(), actRight = currentRobot
				.getRightWheelSpeed();

		// Log data
		NeuralObj neural = new NeuralObj(currLeft, currRight, desLeftCmd,
				desRightCmd, sentLeftCmd, sentRightCmd, actLeft, actRight);
		data.add(neural);

		ts.addElement(neural.ste());
		bp.doOneLearningIteration(ts);

		LOG.trace("Data size " + data.size() + "\nCommands " + neural.save()
				+ "\nSentLeft " + sentLeftCmd + ", ActLeft " + actLeft
				+ ", DesLeft " + desLeftCmd + ", ErrLeft "
				+ neural.getErrLeft() + "\nSentRight " + sentRightCmd
				+ ", ActRight " + actRight + ", DesRight " + desRightCmd
				+ ", ErrRight " + neural.getErrRight() + "\n");

		// Update
		this.sentLeftCmd = left;
		this.sentRightCmd = right;
		this.currLeft = cLeft;
		this.currRight = cRight;
		this.desLeftCmd = gLeft;
		this.desRightCmd = gRight;

		this.lastRobot = currentRobot;

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
