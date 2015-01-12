package main;

import descripteurs.MyDescriptorType;
import evaluators.MyEvaluator;
import evaluators.MyLearnerType;

public class Main {

	public static void main(String[] args) throws Exception {
		System.out.println("--- Start ---");

		/*
		 * simpleExecution("../images/img1.JPG",
		 * MyDescriptorType.AutoColorCorrelogram, MyLearnerType.rProp);
		 * System.out.println();
		 * 
		 * simpleExecution("../images/img2.JPG",
		 * MyDescriptorType.JointHistogram, MyLearnerType.svm);
		 * System.out.println();
		 * 
		 * simpleExecution("../images/img2.JPG", MyDescriptorType.CEDD,
		 * MyLearnerType.tree); System.out.println();
		 */
		// String imagePath =
		// "C:/Users/So/Desktop/csvTestsEtImages/images/coastlineToTest/1395702050.jpg";
		// ApplyModelComputation.simpleExecution(imagePath,
		// MyDescriptorType.AutoColorCorrelogram, MyLearnerType.rProp, "1");

		// ApplyModelComputation.fullSimpleExecution(imagePath, "1");

		MyEvaluator.logResults = false;
		ApplyModelComputation.complexExecution("C:/Users/So/Desktop/csvTestsEtImages/csv/coastlineToTest.csv",
				"C:/Users/So/Desktop/csvTestsEtImages/images/coastlineToTest/", MyDescriptorType.AutoColorCorrelogram,
				MyLearnerType.rProp);

		System.out.println("--- End ---");

	}

}
