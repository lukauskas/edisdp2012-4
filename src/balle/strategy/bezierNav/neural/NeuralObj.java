package balle.strategy.bezierNav.neural;

import java.util.List;

import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingSet;

public class NeuralObj {
	
	protected final double currLeft, currRight;
	protected final int    desLeft, desRight;
	protected final int    outLeft, outRight;
	protected final double actLeft, actRight;
	
	public NeuralObj(double currLeft, double currRight, int desLeft,
			int desRight, int outLeft, int outRight, double actLeft,
			double actRight) {
		this.currLeft = currLeft;
		this.currRight = currRight;
		this.desLeft = desLeft;
		this.desRight = desRight;
		this.outLeft = outLeft;
		this.outRight = outRight;
		this.actLeft = actLeft;
		this.actRight = actRight;
	}

	public double getCurrLeft() {
		return currLeft;
	}

	public double getCurrRight() {
		return currRight;
	}

	public double convCurrLeft() {
		return NeuralNetExecutor.convert(getCurrLeft());
	}

	public double convCurrRight() {
		return NeuralNetExecutor.convert(getCurrRight());
	}

	public int getDesLeft() {
		return desLeft;
	}

	public int getDesRight() {
		return desRight;
	}

	public double convDesLeft() {
		return NeuralNetExecutor.convert(getDesLeft());
	}

	public double convDesRight() {
		return NeuralNetExecutor.convert(getDesRight());
	}

	public int getOutLeft() {
		return outLeft;
	}

	public int getOutRight() {
		return outRight;
	}

	public double convOutLeft() {
		return NeuralNetExecutor.convert(getCurrLeft());
	}

	public double convOutRight() {
		return NeuralNetExecutor.convert(getCurrRight());
	}

	public double getActLeft() {
		return actLeft;
	}

	public double getActRight() {
		return actRight;
	}

	public double convActLeft() {
		return NeuralNetExecutor.convert(getCurrLeft());
	}

	public double convActRight() {
		return NeuralNetExecutor.convert(getCurrRight());
	}

	public static TrainingSet<SupervisedTrainingElement> compile(
			List<NeuralObj> list) {
		TrainingSet<SupervisedTrainingElement> ts = new TrainingSet<SupervisedTrainingElement>();

		for (NeuralObj e : list) {
			double[] inputs = new double[] { e.convDesLeft(), e.convActLeft(),
					e.convDesRight(), e.getActRight() };
			double[] outputs = new double[] {
					(2 * e.convOutLeft()) - e.convActLeft(),
					(2 * e.convOutRight()) - e.convActRight() };

			ts.addElement(new SupervisedTrainingElement(inputs, outputs));
		}

		return ts;
	}
}
