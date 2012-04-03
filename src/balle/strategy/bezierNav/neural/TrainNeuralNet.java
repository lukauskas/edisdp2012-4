package balle.strategy.bezierNav.neural;

import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.MultiLayerPerceptron;

import balle.misc.Globals;

public class TrainNeuralNet {

	public static final int[] layers = new int[] {
 4, 2
	};


	public static void main(String[] args) {
		TrainingSet<SupervisedTrainingElement> ts = Globals.neuralDataFile
				.readTrainingSet();

		MultiLayerPerceptron mlp = new MultiLayerPerceptron(layers);
		

	}

}
