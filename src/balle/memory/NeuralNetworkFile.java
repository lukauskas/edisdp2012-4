package balle.memory;

import java.util.ArrayList;

import balle.memory.utility.FileReadWriter;
import balle.strategy.bezierNav.neural.NeuralSaves;

public class NeuralNetworkFile extends FileReadWriter<NeuralSaves> {

	public NeuralNetworkFile(FolderReader fr, String filename) {
		super(fr, filename);
	}


	@Override
	protected NeuralSaves readBody(ArrayList<String> lines) {
		ArrayList<double[][]> doubleAList = new ArrayList<double[][]>();

		for (int itr = 0; itr < lines.size(); itr++) {
			// get header: tells us how many neuralNets we have
			String line = lines.get(itr);

			if (line.startsWith("£")) {

				String[] head = line.split(";");
				int rows = Integer.parseInt(head[1]);
				int columns = Integer.parseInt(head[2]);

				int startRow = itr + 1;
				double[][] dubArray = new double[columns][rows];

				// split each line into lists of strings using " " to separate
				for (int i = startRow; i < startRow + rows; i++) {
					String[] stringList = lines.get(i).split(" ");

					// characters to doubles
					for (int j = 0; j < columns; j++) {

						double nrlDouble = Double.parseDouble(stringList[j]);
						dubArray[j][i] = nrlDouble;
					}
				}

				doubleAList.add(dubArray);
			}
		}
		NeuralSaves nrlSave = new NeuralSaves(doubleAList);
		return nrlSave;
	}

	@Override
	protected boolean isHeader(String header) {
		return true;
	}

	@Override
	protected String writeHeader() {
		return "";
	}
}
