package balle.memory;

import balle.misc.Powers;

public class PowerConfigFile extends FileReadWriter<Powers> {

	public PowerConfigFile(FolderReader fr, String filename) {
		super(fr, filename);
	}

	@Override
	public Powers read(String line) {
		return (Powers) Powers.load(line);
	}


	@Override
	protected boolean isHeader(String potentialHeader) {
		String[] parts = potentialHeader.split("\t");
		if (parts[0].equals("Power Config File:"))
			return true;
		else
			return false;
	}

	@Override
	protected String writeHeader() {
		String header = "Power Config File:\t";
		return header;
	}

}
