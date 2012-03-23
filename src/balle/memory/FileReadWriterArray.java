package balle.memory;

import java.io.IOException;
import java.util.ArrayList;

class SavesArray<S extends Saves> implements Saves {
	public S[] array;

	@Override
	public String save() {
		String output = "";
		for (S s : array)
			output += s.save();
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

	@SuppressWarnings("unchecked")
	@Override
	public final SavesArray<E> readBody(String[] lines) {
		ArrayList<E> list = new ArrayList<E>();

		for (String line : lines)
			list.add(readLine(line));

		SavesArray<E> output = new SavesArray<E>();
		output.array = (E[]) list.toArray();
		return output;
	}

	@Override
	public final String writeBody(SavesArray<E> eArray) {
		ArrayList<String> output = new ArrayList<String>();

		for (E e : eArray.array)
			output.add(e.save());

		return output.toArray().toString();
	}

	public final void writeArray(E[] e) throws IOException {
		SavesArray<E> eArray = new SavesArray<E>();
		eArray.array = e;
		super.write(eArray);
	}
}
