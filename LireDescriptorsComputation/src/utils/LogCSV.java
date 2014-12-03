package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVWriter;

public class LogCSV {
	private static CSVWriter logWriter;
	private static String FILENAME = "log.csv";
	
	public static int EXISTING_FILE = 1;
	public static int NEW_FILE = 0;
	public static int FILE_ERROR = -1;

	public static int initWriter(File workingDir) {

		int returnCode = FILE_ERROR;
		File csvFile = new File(workingDir, FILENAME);
		FileWriter tempFileWriter = null;
		if(csvFile.exists()) {
			try {
				// Open in append mode
				tempFileWriter = new FileWriter(csvFile, true);
				returnCode = EXISTING_FILE;
			} catch (IOException e) {
				System.out.println(e.getStackTrace());
				return returnCode;
			}
		} else {
			try {
				// Open in create mode
				tempFileWriter = new FileWriter(csvFile, false);
				returnCode = NEW_FILE;
			} catch (IOException e) {
				System.out.println(e.getStackTrace());
				return returnCode;
			}
		}
		logWriter = new CSVWriter(tempFileWriter, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
		return returnCode;
	}

	public static boolean finish() {
		try {
			logWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
