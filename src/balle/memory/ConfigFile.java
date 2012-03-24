package balle.memory;

import java.util.ArrayList;

import balle.main.Config;
import balle.memory.utility.FileReadWriter;

public class ConfigFile extends FileReadWriter<Config> {
	public static final String header = "ConfigFile";

	public ConfigFile(FolderReader fr, String filename) {
		super(fr, filename);
	}

	@Override
	protected Config readBody(ArrayList<String> lines) {
		return Config.load(lines);
	}

	@Override
	protected boolean isHeader(String header) {
		return header.equals(header);
	}

	@Override
	protected String writeHeader() {
		return header;
	}

}
