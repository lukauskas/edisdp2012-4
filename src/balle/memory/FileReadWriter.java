package balle.memory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

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

	public final E read() throws IOException {
		BufferedReader br = fr.getReader(filename);
		
		String nextLine;
		ArrayList<String> lines = new ArrayList<String>();
		if (!isHeader(br.readLine()))
			throw new IOException();

		while ((nextLine = br.readLine()) != null)
			lines.add(nextLine);
		br.close();
		
		E output = readBody((String[]) lines.toArray());
		return (E) output;
	}

	public final void write(E powers) throws IOException {
		BufferedWriter bw = fr.getWriter(filename);

		bw.write(writeHeader() + "\n");
		bw.append(writeBody(powers));
		
		bw.close();
	}

	// Internal

	public abstract E readBody(String[] lines);

	public abstract String writeBody(E e);

	protected abstract boolean isHeader(String header);

	protected abstract String writeHeader();
}