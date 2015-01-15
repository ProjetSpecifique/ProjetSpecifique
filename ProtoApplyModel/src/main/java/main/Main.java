package main;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import evaluators.MyEvaluator;

public class Main {

	private static String textFolderPath = "C:/Users/So/Desktop/javaLogs/";
	private static String csvFolderPath = "C:/Users/So/Desktop/TermCSV/set10Janv2015/";
	private static String imgFolderPath = "C:/Users/So/Desktop/PS-Images/";

	public static void main(String[] args) throws Exception {
		System.out.println("--- Start ---");

		// String imagePath = "C:/Users/So/Desktop/PS-Images/1395702050.jpg";
		// ApplyModelComputation.simpleExecution(imagePath,
		// MyDescriptorType.AutoColorCorrelogram, MyLearnerType.rProp,
		// MyTerm.coastline, "1");
		// ApplyModelComputation.fullSimpleExecution(imagePath, "1");

		MyEvaluator.logResults = false;
		for (MyTerm term : MyTerm.values()) {
			setConsoleToFile(textFolderPath + term.name() + "Logs.txt");
			ApplyModelComputation.fullComplexExecution(csvFolderPath + term.name() + ".csv", imgFolderPath, term);
		}

		// ApplyModelComputation.complexExecution(csvPath, imgFolderPath,
		// MyDescriptorType.BasicFeatures, MyLearnerType.tree,
		// MyTerm.coastline);

		setDefaulConsole();
		System.out.println("--- End ---");

	}

	private static void setConsoleToFile(String path) throws FileNotFoundException {
		FileOutputStream consoleFile = new FileOutputStream(path);

		System.setOut(new PrintStream(consoleFile));
	}

	private static void setDefaulConsole() {
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
	}
}
