package main;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.imageio.ImageIO;

import descripteurs.MyDescriptor;
import descripteurs.MyDescriptorFactory;
import descripteurs.MyDescriptorType;
import evaluators.MyEvalPathFactory;
import evaluators.MyEvaluator;
import evaluators.MyLearnerType;

public class ApplyModelComputation {

	private static final String imageExtention = ".jpg";
	private static final String csvSeparator = ";";

	/**
	 * Computes the model probability for a single image
	 * 
	 * @param imagePath
	 *            : image path
	 * @param descriptorType
	 *            : the type of descriptor to apply
	 * @param learnerType
	 *            : the type of learner to apply
	 * 
	 * @return probability : 0 to 1
	 * */
	public static String simpleExecution(String imagePath, MyDescriptorType descriptorType, MyLearnerType learnerType,
			MyTerm term, String resultClass) throws Exception {
		/* 1. init image */
		BufferedImage image;
		try {
			image = ImageIO.read(new File(imagePath));
			// for relative paths
			// image = ImageIO.read(Main.class.getResourceAsStream(imagePath));
		} catch (Exception e) {
			throw new Exception("Cannot load image at : " + imagePath);
		}

		/* 2. comoute histogram */
		MyDescriptor myDescriptor = MyDescriptorFactory.buildDescriptor(descriptorType, image);
		double[] histogram = myDescriptor.computeHistogram();
		// System.out.println(histogram.length);

		/* 3. Compute model path */
		String modelPath = MyEvalPathFactory.buildModelPath(descriptorType, learnerType, term);

		// Statistics stats = new Statistics(histogram);
		// System.out.println(stats.getMean() + " " + stats.getVariance() + " "
		// + stats.getStdDev() );

		/* 3. apply model */
		return MyEvaluator.evaluate(histogram, modelPath, resultClass);
		// MyNaiveBayesEvaluator.evaluate(histogram);
	}

	/**
	 * Computes statistics based on the probability of model for all images in a
	 * csv
	 * 
	 * 
	 * */
	public static void complexExecution(String csvPath, String imageFolderPath, MyDescriptorType descriptorType,
			MyLearnerType learnerType, MyTerm term) {
		// http://fr.wikipedia.org/wiki/Pr%C3%A9cision_et_rappel
		// tp = true positive = items correctly labeled as belonging to the
		// class
		int tp0 = 0, tp1 = 0;
		// fp = false positive = items incorrectly labeled as belonging to the
		// class
		int fp0 = 0, fp1 = 0;
		// fn = false negative = items which were not labeled as belonging to
		// the positive class but should have been
		int fn0 = 0, fn1 = 0;

		// fn0 = fp1 and fn1 = fp0

		BufferedReader br = null;
		String line = "";
		String imgPath;
		String modelWinnerClass;

		System.out.println("-----------------------------------------");
		System.out.println("Csv : " + csvPath);
		System.out.println("Term : " + term);
		System.out.println("Descriptor : " + descriptorType);
		System.out.println("Learner : " + learnerType);

		try {
			br = new BufferedReader(new FileReader(csvPath));
			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] elems = line.split(csvSeparator);
				imgPath = imageFolderPath + elems[0] + imageExtention;

				// System.out.println("csv Winner :  " + elems[1]);
				modelWinnerClass = simpleExecution(imgPath, descriptorType, learnerType, term, elems[1]);

				if (modelWinnerClass != null && modelWinnerClass.equals(elems[1])) {
					// correctly labeled
					if ("1".equals(elems[1])) {
						tp1++;
					} else {
						tp0++;
					}
				} else {
					// incorrectly labeled
					if ("1".equals(elems[1])) {
						// items were label 0 but they were 1
						fp0++;
						fn1++;
					} else {
						// items were label 1 but they were 0
						fp1++;
						fn0++;
					}
				}
			}
			br.close();

			System.out.println("Analysed Images : " + (tp0 + fp0 + tp1 + fp1));
			System.out.println(tp0 + " " + fp0 + " " + fn0);
			System.out.println(tp1 + " " + fp1 + " " + fn1);
			float precision0 = (float) tp0 / (tp0 + fp0), precision1 = (float) tp1 / (tp1 + fp1);
			System.out.println("Precision class 1 :" + precision1);
			System.out.println("Precision class 0 :" + precision0);
			System.out.println("Precision :" + (precision0 + precision1) / 2);
			float recall0 = (float) tp0 / (tp0 + fn0), recall1 = (float) tp1 / (tp1 + fn1);
			System.out.println("Recall class 1 :" + recall1);
			System.out.println("Recall class 0 :" + recall0);
			System.out.println("Recall :" + (recall0 + recall1) / 2);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		System.out.println("-----------------------------------------\n");
	}

	public static void fullSimpleExecution(String imagePath, String resultClass) {

		for (MyDescriptorType descriptorType : MyDescriptorType.values()) {
			for (MyLearnerType learnerType : MyLearnerType.values()) {
				for (MyTerm term : MyTerm.values()) {

					try {
						// System.out.println("\n Descriptor Type : " +
						// descriptorType + " Learner Type : " + learnerType);
						simpleExecution(imagePath, descriptorType, learnerType, term, resultClass);
					} catch (Exception e) {
						// System.err.println(e);
					}
				}
			}
		}
	}

	public static void fullComplexExecution(String csvPath, String imageFolderPath, MyTerm term) {

		for (MyDescriptorType descriptorType : MyDescriptorType.values()) {
			for (MyLearnerType learnerType : MyLearnerType.values()) {
				complexExecution(csvPath, imageFolderPath, descriptorType, learnerType, term);
			}
		}
	}
}
