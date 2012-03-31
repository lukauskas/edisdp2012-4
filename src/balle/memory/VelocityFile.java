package balle.memory;

import balle.memory.utility.FileReadWriterArray;
import balle.world.Velocity;

public class VelocityFile extends FileReadWriterArray<Velocity> {
	public static final String IDENTIFIER = "Velocity Save File:";

	public VelocityFile(FolderReader fr, String filename) {
		super(fr, filename);
	}

	@Override
	public Velocity readLine(String line) {
		return Velocity.load(line);
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
