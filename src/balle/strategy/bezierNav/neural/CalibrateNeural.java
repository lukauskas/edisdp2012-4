package balle.strategy.bezierNav.neural;

import org.neuroph.core.learning.TrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.MultiLayerPerceptron;

import balle.controller.Controller;
import balle.strategy.planner.AbstractPlanner;
import balle.world.Snapshot;

public class CalibrateNeural extends AbstractPlanner {

	protected MultiLayerPerceptron mlp;
	protected NeuralNetExecutor nne;

	protected TrainingSet ts;

	public CalibrateNeural() {
		mlp = new MultiLayerPerceptron(4, 5, 5, 2);
		mlp.initializeWeights(0.5);

		nne = new NeuralNetExecutor(mlp);
		nne.addlistener(this);

		ts = new TrainingSet();
	}

	@Override
	protected void onStep(Controller controller, Snapshot snapshot) {


	}

	public void listen(TrainingElement te) {
		ts.addElement(te);
	}

}
