package balle.memory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import balle.misc.Powers;

public abstract class FileReadWriter<E extends Saves> {
	
	// Instance
	
	protected final String filename;

	protected final FolderReader fr;
	
	// Constructors
	
	public FileReadWriter(FolderReader fr, String filename) {
		this.fr = fr;
		this.filename = filename;
	}
	
	// Interface
	
	public abstract E read(String line);

	protected abstract boolean isHeader(String header);

	protected abstract String writeHeader();

	public Powers[] read() throws IOException {
		BufferedReader br = fr.getReader(filename);
		
		ArrayList<E> output = new ArrayList<E>();
		
		String nextLine;
		if (!isHeader(br.readLine()))
			throw new IOException();

		while ((nextLine = br.readLine()) != null)
			output.add(read(nextLine));
		
		br.close();
		return (Powers[]) output.toArray();
	}

	public void write(E[] data) throws IOException {
		BufferedWriter bw = fr.getWriter(filename);

		bw.write(writeHeader() + "\n");
		
		for (E e : data)
			bw.append(e.save() + "\n");
		
		bw.close();
	}

}
