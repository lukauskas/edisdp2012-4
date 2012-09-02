package balle.memory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FolderReader {

	// Instance

	protected final String foldername;

	// Constructor

	public FolderReader(String foldername) {
		this.foldername = foldername;

		// Ensure Folder exists
		File folder = new File(foldername);
		if (!folder.exists())
			folder.mkdir();
	}

	public FolderReader(FolderReader parent, String foldername) {
		this(parent.foldername + "/" + foldername);
	}

	// Interface

	public File getFile(String filename) {
		return new File(foldername + "/" + filename);
	}


	public BufferedWriter getWriter(String filename)
			throws FileNotFoundException {
		return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
				new File(foldername + "/" + filename))));
	}
	public BufferedReader getReader(String filename)
			throws FileNotFoundException {
		return new BufferedReader(new InputStreamReader(new FileInputStream(
				new File(foldername + "/" + filename))));
	}

	public String readFile(String filename) throws IOException {
		BufferedReader br = getReader(foldername + "/" + filename);
		String nextline = null, out = "";

		// Concatenate all lines together in one string.
		while ((nextline = br.readLine()) != null)
			out += nextline + "\n";

		return out;
	}

	public void writeFile(String filename, String body) throws IOException {
		BufferedWriter bw = getWriter(foldername + "/" + filename);
		String[] lines = body.split("\n");

		for (String line : lines)
			bw.append(line);
	}

	// Internal

}
