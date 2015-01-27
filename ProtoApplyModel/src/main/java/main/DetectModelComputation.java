package main;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import descripteurs.MyDescriptor;
import descripteurs.MyDescriptorFactory;
import descripteurs.MyDescriptorType;
import evaluators.MyEvalPathFactory;
import evaluators.MyEvaluator;
import evaluators.MyLearnerType;

public class DetectModelComputation {

	/*
	 * for an image compute the best model for all terms
	 */
	public static String detectModelForTerm(String imagePath, MyTerm term) throws Exception {
		/* 1. init image */
		BufferedImage image;
		try {
			image = ImageIO.read(new File(imagePath));
			// for relative paths
			// image = ImageIO.read(Main.class.getResourceAsStream(imagePath));
		} catch (Exception e) {
			throw new Exception("Cannot load image at : " + imagePath);
		}

		Double bestProba = 0.0, currProba;
		MyDescriptorType myBestDescriptor = null;
		MyLearnerType myBestLearner = null;
		MyDescriptor myDescriptor;
		double[] histogram;
		String modelPath;

		for (MyDescriptorType descriptorType : MyDescriptorType.values()) {
			for (MyLearnerType learner : MyLearnerType.values()) {
				myDescriptor = MyDescriptorFactory.buildDescriptor(descriptorType, image);
				histogram = myDescriptor.computeHistogram();

				modelPath = MyEvalPathFactory.buildModelPath(descriptorType, learner, term);
				currProba = MyEvaluator.evaluateProbability(histogram, modelPath, "1");

				if (currProba > bestProba) {
					myBestDescriptor = descriptorType;
					myBestLearner = learner;
					bestProba = currProba;
				}
			}
		}

		return "Term : " + term + "\nBest Probility : " + bestProba + " " + myBestDescriptor + " " + myBestLearner;
	}

	public static String detectModelForAllTerms(String imagePath) throws Exception {
		String results = "";

		for (MyTerm term : MyTerm.values()) {
			results += "\n" + detectModelForTerm(imagePath, term);
		}

		return results;
	}
}
