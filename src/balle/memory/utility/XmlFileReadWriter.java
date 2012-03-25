package balle.memory.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import balle.memory.FolderReader;

public abstract class XmlFileReadWriter<E extends XmlSaves> extends
		FileReadWriter<E> {
	protected static String MAKE_COMMENT(String line) {
		return "<!--" + line + "-->";
	}
	
	// Instance

	protected final DocumentBuilderFactory docBuilderFactory;
	protected final DocumentBuilder docBuilder;

	// Constructor

	public XmlFileReadWriter(FolderReader fr, String filename)
			throws SAXException, IOException, ParserConfigurationException {
		super(fr, filename);
		docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilder = docBuilderFactory.newDocumentBuilder();
	}

	@Override
	public E read() throws IOException {
		BufferedReader br = fr.getReader(filename);
		if (!isHeader(br.readLine()))
			throw new IOException();

		Document doc;

		try {
			doc = docBuilder.parse(file);

		} catch (SAXException e) {
			throw new IOException();
		}

		doc.getDocumentElement().normalize();

		return read(doc);
	}

	public abstract E read(Document doc);

	@Override
	public void write(E e) throws IOException {
		BufferedWriter bw = fr.getWriter(filename);
		bw.write(writeHeader());

		Document doc;

		try {
			doc = docBuilder.parse(file);

		} catch (SAXException ex) {
			throw new IOException();
		}

		((XmlSaves) e).save(doc);
	}

	// Interface.
	// Don't do these things

	protected E readBody(String[] lines) {
		return null;
	}

	protected String writeBody(E e) {
		return null;
	}
}
