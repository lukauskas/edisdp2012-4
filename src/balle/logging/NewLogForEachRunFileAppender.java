package balle.logging;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.ErrorCode;

// Daniel: Example I found online that outputs the logging data
// to a new file after each run of the program.

// Outputs to a file "Data-... .log"
// The "..." is System.currentTimeMillis(). 

/**
 * This is a customized log4j appender, which will create a new file for every
 * run of the application.
 * 
 * @author veera | http://veerasundar.com
 * 
 */
public class NewLogForEachRunFileAppender extends FileAppender {

	public NewLogForEachRunFileAppender() {
	}

	public NewLogForEachRunFileAppender(Layout layout, String filename,
			boolean append, boolean bufferedIO, int bufferSize)
			throws IOException {
		super(layout, filename, append, bufferedIO, bufferSize);
	}

	public NewLogForEachRunFileAppender(Layout layout, String filename,
			boolean append) throws IOException {
		super(layout, filename, append);
	}

	public NewLogForEachRunFileAppender(Layout layout, String filename)
			throws IOException {
		super(layout, filename);
	}

	public void activateOptions() {
		if (fileName != null) {
			try {
				fileName = getNewLogFileName();
				setFile(fileName, fileAppend, bufferedIO, bufferSize);
			} catch (Exception e) {
				errorHandler.error("Error while activating log options", e,
						ErrorCode.FILE_OPEN_FAILURE);
			}
		}
	}

	private String getNewLogFileName() {
		if (fileName != null) {
			final File logFile = new File(fileName);
			final String fileName = logFile.getName();
			String newFileName = "";

			final int dotIndex = fileName.indexOf(".");
			if (dotIndex != -1) {
				// the file name has an extension. so, insert the time stamp
				// between the file name and the extension
				newFileName = fileName.substring(0, dotIndex) + "-"
						+ ((+System.currentTimeMillis()) / 1000) + "."
						+ fileName.substring(dotIndex + 1);
			} else {
				// the file name has no extension. So, just append the timestamp
				// at the end.
				newFileName = fileName + "-" + System.currentTimeMillis();
			}
			return logFile.getParent() + File.separator + newFileName;
		}
		return null;
	}
}
