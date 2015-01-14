package main;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.google.common.primitives.Doubles;

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
	public static Double simpleExecution(String imagePath, MyDescriptorType descriptorType, MyLearnerType learnerType,
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
		List<Double> positiveValues = new ArrayList<Double>();
		List<Double> negativeValues = new ArrayList<Double>();

		BufferedReader br = null;
		String line = "";
		String imgPath;
		Double probability;

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
				probability = simpleExecution(imgPath, descriptorType, learnerType, term, elems[1]);

				if ("1".equals(elems[1])) {
					// positive class value
					positiveValues.add(probability);
				} else {
					// negative class value
					negativeValues.add(probability);
				}
			}
			br.close();

			Statistics statistics;
			System.out.println("Statistics for Postive values");
			if (positiveValues.size() > 0) {
				statistics = new Statistics(Doubles.toArray(positiveValues));
				statistics.writeStatistics();
			} else {
				System.out.println("No positive values");
			}

			System.out.println("Statistics for Negative values");
			if (negativeValues.size() > 0) {
				statistics = new Statistics(Doubles.toArray(negativeValues));
				statistics.writeStatistics();
			} else {
				System.out.println("No negative values");
			}
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