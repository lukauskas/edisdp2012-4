package balle.memory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import balle.misc.Powers;

public class PwrVeloFile {
	
	// Instance
	
	protected final String filename;

	protected final FolderReader fr;
	
	// Constructors
	
	public PwrVeloFile(FolderReader fr, String filename) {
		this.fr = fr;
		this.filename = filename;
	}
	
	// Interface
	
	public Powers[] read() throws IOException {
		BufferedReader br = fr.getReader(filename);
		
		ArrayList<Powers> output = new ArrayList<Powers>();
		
		String nextLine;
		if (!isHeader(br.readLine()))
			throw new IOException();

		while ( (nextLine = br.readLine()) != null ) {
			String[] tokens = nextLine.split(";");
			int power = Integer.parseInt(tokens[0]);
			float velocity = Float.parseFloat(tokens[1]);
			output.add(new Powers(power, velocity));
		}
		
		br.close();
		return (Powers[]) output.toArray();
	}

	public void write(Powers[] data) throws IOException {
		BufferedWriter bw = fr.getWriter(filename);

		bw.write(writeHeader() + "\n");
		
		for (Powers p : data) 
			bw.append(p.toString() + "\n");
		
		bw.close();
	}

	// Internal

	protected boolean isHeader(String potentialHeader) {
		String[] parts = potentialHeader.split("\t");
		if (parts[0].equals("Power Config File:"))
			return true;
		else 
			return false;
	}

	protected String writeHeader() {
		String header = "Power Config File:\t";
		return header;
	}

}
