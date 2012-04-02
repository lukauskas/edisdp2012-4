package balle.strategy.bezierNav.neural;

import org.apache.log4j.Logger;
import org.neuroph.core.learning.TrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;

import balle.controller.Controller;
import balle.strategy.FactoryMethod;
import balle.strategy.planner.AbstractPlanner;
import balle.world.Snapshot;
import balle.world.objects.Robot;

public class CalibrateNeural extends AbstractPlanner {

	public static final Logger LOG = Logger.getLogger(CalibrateNeural.class);

	protected MultiLayerPerceptron mlp;
	protected NeuralNetExecutor nne;

	protected TrainingSet ts;

	protected int sentLeftCmd = 0, sentRightCmd = 0;
	protected double desLeftCmd = 0, desRightCmd = 0;

	protected Robot lastRobot = null;

	@FactoryMethod(designator = "CalibrateNeural", parameterNames = {})
	public static CalibrateNeural calibrateNeuralFactory() {
		return new CalibrateNeural();
	}

	public CalibrateNeural() {
		mlp = new MultiLayerPerceptron(4, 5, 5, 2);
		mlp.setLearningRule(new BackPropagation());

		mlp.initializeWeights(0.5);

		nne = new NeuralNetExecutor(mlp);
		nne.addlistener(this);

		ts = new TrainingSet();
	}

	@Override
	protected void onStep(Controller controller, Snapshot snapshot) {


	}

	protected void record(int gLeft, int gRight, double left, double right) {
		double lastLeft = lastRobot.getLeftWheelSpeed(), lastRight = lastRobot.getRightWheelSpeed();
		TrainingElement te = new TrainingElement(desLeftCmd, desRightCmd,
				lastLeft, lastRight);

		ts.addElement(te);

		this.sentLeftCmd = (int) left;
		this.sentRightCmd = (int) right;
		this.desLeftCmd = gLeft;
		this.desRightCmd = gRight;
	}

}
