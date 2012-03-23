package balle.memory.utility;

import balle.memory.FolderReader;

public class XmlFileReadWriter<E extends XmlSaves> extends FileReadWriter<E> {

	public XmlFileReadWriter(FolderReader fr, String filename) {
		super(fr, filename);
	}

	@Override
	public E readBody(String[] lines) {
		return null;
	}

	@Override
	public String writeBody(E e) {
		return null;
	}

	@Override
	protected boolean isHeader(String header) {
		return false;
	}

	@Override
	protected String writeHeader() {
		return null;
	}

}
