package main;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import evaluators.MyEvaluator;

public class Main {

	public static String textFilePath = "C:/Users/So/Desktop/protoLogs.txt";
	public static FileOutputStream consoleFile = null;

	public static void main(String[] args) throws Exception {
		System.out.println("--- Start ---");

		// String imagePath = "C:/Users/So/Desktop/PS-Images/1395702050.jpg";
		// ApplyModelComputation.simpleExecution(imagePath,
		// MyDescriptorType.AutoColorCorrelogram, MyLearnerType.rProp,
		// MyTerm.coastline, "1");
		// ApplyModelComputation.fullSimpleExecution(imagePath, "1");

		String csvPath = "C:/Users/So/Desktop/TermCSV/set10Janv2015/coastline.csv";
		String imgFolderPath = "C:/Users/So/Desktop/PS-Images/";

		MyEvaluator.logResults = false;
		setConsoleToFile();
		// ApplyModelComputation.complexExecution(csvPath, imgFolderPath,
		// MyDescriptorType.BasicFeatures, MyLearnerType.tree,
		// MyTerm.coastline);
		ApplyModelComputation.fullComplexExecution(csvPath, imgFolderPath, MyTerm.coastline);

		setDefaulConsole();
		System.out.println("--- End ---");

	}

	private static void setConsoleToFile() throws FileNotFoundException {
		if (consoleFile == null) {
			consoleFile = new FileOutputStream(textFilePath);
		}

		System.setOut(new PrintStream(consoleFile));
	}

	private static void setDefaulConsole() {
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
	}
}
