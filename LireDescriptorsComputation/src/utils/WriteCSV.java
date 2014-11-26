package utils;

import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteCSV {

	private static CSVWriter writer;
	private static String EXTENSION = ".csv";

	public static boolean initWriter(File workingDir, String filePath) {

		File csvFile = new File(workingDir, filePath + EXTENSION);
		try {
			writer = new CSVWriter(new FileWriter(csvFile), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public static void writeHistogramLine(String imgName, double[] histogram) {

		String record[] = new String[histogram.length + 1];
		record[0] = imgName;
		for (int i = 0; i < histogram.length; ++i) {
			record[i + 1] = Double.toString(histogram[i]);
		}
		writer.writeNext(record);
	}

	public static void writeLine(String imgName, double[] histogram, int cluster) {

		String record[] = new String[histogram.length + 2];
		record[0] = imgName;
		for (int i = 0; i < histogram.length; ++i) {
			record[i + 1] = Double.toString(histogram[i]);
		}
		record[histogram.length] = Integer.toString(cluster);
		writer.writeNext(record);

		// TODO : try and see which option is faster
		// String[] attempt = Arrays.copyOf(histogram, histogram.length,
		// String[].class);
	}

	public static boolean finish() {
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
