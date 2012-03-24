package balle.memory.utility;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import balle.memory.FolderReader;

class XmlLeaf {

	private XmlSaves e;

	public XmlLeaf(XmlSaves e) {
		this.e = e;
	}

	public XmlSaves get() {
		return e;
	}

}

class XmlElement implements XmlSaves {

	public XmlLeaf[] children;

	@Override
	public String save() {
		return null;
	}

	@Override
	public void save(Document d) {
		
	}

}

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
		Document doc;

		try {
			doc = docBuilder.parse(file);

		} catch (SAXException e) {
			throw new IOException();
		}


		return null;
	}

	@Override
	public void write(E powers) throws IOException {
		BufferedWriter bw = fr.getWriter(filename);

		bw.write(writeHeader() + "\n");
		bw.append(writeBody(powers));

		bw.close();
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
