package main;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import descripteurs.MyDescriptor;
import descripteurs.MyDescriptorFactory;
import descripteurs.MyDescriptorType;
import evaluators.MyEvalPathFactory;
import evaluators.MyEvaluator;
import evaluators.MyLearnerType;

public class Main {

	public static void main(String[] args) throws Exception {
		System.out.println("--- Start ---");

		simpleExecution("../images/img1.JPG", MyDescriptorType.AutoColorCorrelogram, MyLearnerType.rProp);
		System.out.println();
		
		simpleExecution("../images/img2.JPG", MyDescriptorType.AutoColorCorrelogram, MyLearnerType.rProp);
		System.out.println();
		
		simpleExecution("../images/img2.JPG", MyDescriptorType.CEDD, MyLearnerType.tree);

		System.out.println("--- End ---");

	}

	private static void simpleExecution(String imagePath, MyDescriptorType descriptorType, MyLearnerType learnerType)
			throws Exception {
		/* 1. init image */
		BufferedImage image = ImageIO.read(Main.class.getResourceAsStream(imagePath));

		/* 2. comoute histogram */
		MyDescriptor myDescriptor = MyDescriptorFactory.buildDescriptor(descriptorType, image);
		double[] histogram = myDescriptor.computeHistogram();
		// System.out.println(histogram.length);

		/* 3. Compute model path */
		String modelPath = MyEvalPathFactory.buildModelPath(descriptorType, learnerType);

		/* 3. apply model */
		MyEvaluator.evaluate(histogram, modelPath);
	}
}
