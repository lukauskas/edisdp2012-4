package balle.memory;

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
		return header.split("\t").equals(IDENTIFIER);
	}

	@Override
	protected String writeHeader() {
		return IDENTIFIER;
	}

}