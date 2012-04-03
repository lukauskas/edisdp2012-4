package balle.memory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingSet;

import balle.memory.utility.FileReadWriterArray;
import balle.strategy.bezierNav.neural.NeuralObj;

public class NeuralDataFile extends FileReadWriterArray<NeuralObj> {
	public static final String IDENTIFIER = "Neural Data File:";

	public NeuralDataFile(FolderReader fr, String filename) {
		super(fr, filename);
	}

	@Override
	public NeuralObj readLine(String line) {
		return NeuralObj.load(line);
	}

	@Override
	protected boolean isHeader(String header) {
		return true; // TODO header.split("\t").equals(IDENTIFIER);
	}

	@Override
	protected String writeHeader() {
		return IDENTIFIER;
	}

	public TrainingSet<SupervisedTrainingElement> readTrainingSet() {
		List<NeuralObj> list = new ArrayList<NeuralObj>();

		try {
			list = readArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return NeuralObj.compile(list);
	}

}