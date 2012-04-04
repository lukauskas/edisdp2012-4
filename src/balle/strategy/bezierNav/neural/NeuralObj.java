package balle.strategy.bezierNav.neural;

import java.util.List;

import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingSet;

import balle.memory.utility.Saves;

public class NeuralObj implements Saves {
	
	protected final double currLeft, currRight;
	protected final double desLeft, desRight;
	protected final double errLeft, errRight;
	
	public NeuralObj(double currLeft, double currRight, double desLeft,
			double desRight, double outLeft, double outRight, double actLeft,
			double actRight) {
		this.currLeft = currLeft;
		this.currRight = currRight;
		this.desLeft = desLeft;
		this.desRight = desRight;
		this.errLeft = calcError(desLeft, actLeft, outLeft);
		this.errRight = calcError(desRight, actRight, outRight);
	}

	private double calcError(double des, double act, double out) {
		return out + des - act;
	}

	public double getCurrLeft() {
		return currLeft;
	}

	public double getCurrRight() {
		return currRight;
	}

	public double getDesLeft() {
		return desLeft;
	}

	public double getDesRight() {
		return desRight;
	}

	public double getErrLeft() {
		return errLeft;
	}

	public double getErrRight() {
		return errRight;
	}

	public SupervisedTrainingElement ste() {
		double[] inputs = new double[] { getDesLeft(), getCurrLeft(),
				getDesRight(), getCurrRight() };
		double[] outputs = new double[] { getErrLeft(), getErrRight() };

		return new SupervisedTrainingElement(inputs, outputs);
	}

	public static TrainingSet<SupervisedTrainingElement> compile(
			List<NeuralObj> list) {
		TrainingSet<SupervisedTrainingElement> ts = new TrainingSet<SupervisedTrainingElement>();

		for (NeuralObj e : list) {
			ts.addElement(e.ste());
		}

		return ts;
	}

	// Saving/Loading

	public static NeuralObj load(String line) {
		String[] tokens = line.split(";");
		return new NeuralObj(Double.parseDouble(tokens[0]),
				Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]),
				Double.parseDouble(tokens[3]), Double.parseDouble(tokens[4]),
				Double.parseDouble(tokens[5]), Double.parseDouble(tokens[6]),
				Double.parseDouble(tokens[7]));

	}

	@Override
	public String save() {
		return currLeft + ";" + currRight + ";" + desLeft + ";" + desRight
				+ ";" + errLeft + ";" + errRight;
	}
}
