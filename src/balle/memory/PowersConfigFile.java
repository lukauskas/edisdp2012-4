package balle.memory;

import balle.memory.utility.FileReadWriterArray;
import balle.misc.Powers;

public class PowersConfigFile extends FileReadWriterArray<Powers> {
	public static final String IDENTIFIER = "Power Config File:";

	public PowersConfigFile(FolderReader fr, String filename) {
		super(fr, filename);
	}

	@Override
	public Powers readLine(String line) {
		return Powers.load(line);
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
