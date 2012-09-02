package balle.memory.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import balle.memory.FolderReader;

class SavesArray<S extends Saves> implements Saves {
	public List<S> array;

	@Override
	public String save() {
		String output = "";
		for (S s : array)
			output += s.save() + "\n";
		return output;
	}
}

/**
 * For writing files that contain a list of elements. As an example see
 * PowersConfigFile that can store an array of powers objects.
 * 
 * 
 * @author s0952880
 * 
 * @param <E>
 *            Elements you want written to a file.
 */
public abstract class FileReadWriterArray<E extends Saves> extends
		FileReadWriter<SavesArray<E>> {

	public FileReadWriterArray(FolderReader fr, String filename) {
		super(fr, filename);
	}

	public abstract E readLine(String line);

	@Override
	public final SavesArray<E> readBody(ArrayList<String> lines) {
		ArrayList<E> list = new ArrayList<E>();

		for (String line : lines)
			if (!line.startsWith("#"))
				list.add(readLine(line));

		SavesArray<E> output = new SavesArray<E>();
		output.array = list;
		return output;
	}

	public final List<E> readArray() throws IOException {
		SavesArray<E> sa = read();
		return sa.array;
	}

	@Override
	public final String writeBody(SavesArray<E> eArray) {
		return eArray.save();
	}

	public final void writeArray(List<E> e) throws IOException {
		SavesArray<E> eArray = new SavesArray<E>();
		eArray.array = e;
		write(eArray);
	}

	// Override superclass.

	@Override
	public final SavesArray<E> read() throws IOException {
		BufferedReader br = fr.getReader(filename);

		String nextLine;
		ArrayList<String> lines = new ArrayList<String>();
		if (!isHeader(br.readLine()))
			throw new IOException();

		while ((nextLine = br.readLine()) != null)
			lines.add(nextLine);
		br.close();

		SavesArray<E> output = readBody(lines);
		return (SavesArray<E>) output;
	}

	@Override
	public final void write(SavesArray<E> powers) throws IOException {
		BufferedWriter bw = fr.getWriter(filename);

		bw.write(writeHeader() + "\n");
		bw.append(writeBody(powers));

		bw.close();
	}

}
