package balle.memory.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import balle.memory.FolderReader;

public abstract class FileReadWriter<E extends Saves> {
	
	// Instance
	
	protected final File file;

	protected final String filename;

	protected final FolderReader fr;
	
	// Constructors
	
	public FileReadWriter(FolderReader fr, String filename) {
		this.fr = fr;
		this.filename = filename;
		this.file = new File(filename);
	}
	
	// Interface

	public E read() throws IOException {
		BufferedReader br = fr.getReader(filename);
		
		String nextLine;
		ArrayList<String> lines = new ArrayList<String>();
		if (!isHeader(br.readLine()))
			throw new IOException();

		while ((nextLine = br.readLine()) != null)
			lines.add(nextLine);
		br.close();
		
		E output = readBody(lines);
		return (E) output;
	}

	public void write(E powers) throws IOException {
		BufferedWriter bw = fr.getWriter(filename);

		bw.write(writeHeader() + "\n");
		bw.append(writeBody(powers));
		
		bw.close();
	}

	protected String writeBody(E e) {
		return e.save();
	}

	// Internal

	protected abstract E readBody(ArrayList<String> lines);

	protected abstract boolean isHeader(String header);

	protected abstract String writeHeader();
}
