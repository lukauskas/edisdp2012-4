package balle.strategy.bezierNav.neural;

import java.util.ArrayList;

import balle.memory.utility.Saves;

public class NeuralSaves implements Saves {

	protected ArrayList<double[][]> neuralNetwork;
	
	public NeuralSaves(ArrayList<double[][]> array) {
		this.neuralNetwork = array;
	}

	@Override
	public String save() {
		// TODO add header
		// iterate through array, adding newlines when needed, saving whole
		// thing as String
		String str = "";

		for (double[][] each : neuralNetwork) {

			int rows = each.length;
			int columns = each[0].length;
			String head = ";" + rows + ";" + columns + "\n";
			//
			for (int row = 0; row < each.length; row++) {
				for (int column = 0; column < each[0].length; column++) {
					str += each[column][row] + " ";

				}
				str += "\n";
			}
		}

		return str;

	}

}
