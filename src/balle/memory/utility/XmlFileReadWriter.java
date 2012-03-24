package balle.memory.utility;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import balle.memory.FolderReader;

public class XmlFileReadWriter<E extends XmlSaves> extends FileReadWriter<E> {
	
	protected DocumentBuilderFactory docBuilderFactory;
	protected DocumentBuilder docBuilder;
	protected Document doc;

	public XmlFileReadWriter(FolderReader fr, String filename)
			throws SAXException, IOException, ParserConfigurationException {
		super(fr, filename);
		docBuilderFactory = DocumentBuilderFactory.newInstance();
		doc = docBuilder.parse(file);
		docBuilder = docBuilderFactory.newDocumentBuilder();
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
