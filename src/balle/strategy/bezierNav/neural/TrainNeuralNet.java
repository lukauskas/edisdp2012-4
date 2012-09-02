package balle.strategy.bezierNav.neural;

import java.io.IOException;
import java.util.List;

import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;

import balle.misc.Globals;

public class TrainNeuralNet {

	public static final int[] layers = new int[] {
 4, 2
	};


	public static void main(String[] args) throws IOException {
		List<NeuralObj> list = Globals.neuralDataFile.readArray();

		TrainingSet<SupervisedTrainingElement> ts = NeuralObj.compile(list);

		MultiLayerPerceptron mlp = new MultiLayerPerceptron(layers);
		mlp.randomizeWeights();

		System.out.println(list.size() + "\t" + ts.size());
		System.out.println("Starting learning");
		BackPropagation bp = new BackPropagation();
		bp.setNeuralNetwork(mlp);
		bp.doLearningEpoch(ts);
		System.out.println("AMERICA");
	}

}
