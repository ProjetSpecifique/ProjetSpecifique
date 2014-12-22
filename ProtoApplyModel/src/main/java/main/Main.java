package main;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import descripteurs.MyDescriptor;
import descripteurs.MyDescriptorFactory;
import descripteurs.MyDescriptorType;
import evaluators.MyNaiveBayesEvaluator;

public class Main {

	public static String imagePath = "../images/img2.png";

	public static void main(String[] args) throws Exception {
		System.out.println("--- Start ---");

		/* 1. init image */
		BufferedImage image = ImageIO.read(Main.class.getResourceAsStream(imagePath));
		/* 2. comoute histogram*/
		MyDescriptor myDescriptor = MyDescriptorFactory.buildDescriptor(MyDescriptorType.AutoColorCorrelogram, image);
		double[] histogram = myDescriptor.computeHistogram();
		//System.out.println(histogram.length);
		
		/* 3. apply model*/
		MyNaiveBayesEvaluator.evaluate(histogram);
		
		
		System.out.println("--- End ---");

	}
}
