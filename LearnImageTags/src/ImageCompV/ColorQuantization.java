package ImageCompV;

import javax.print.attribute.standard.MediaSize.JIS;

import de.lmu.ifi.dbs.jfeaturelib.features.Histogram;
import ij.process.ColorProcessor;

public class ColorQuantization {

	private ColorProcessor image1;
	private ColorProcessor image2;

	public ColorQuantization(ColorProcessor image1, ColorProcessor image2) {
		this.image1 = image1;
		this.image2 = image2;
	}

	public double getColorQuantizationEuclidianDistance() {
		ImageDesc desc = new ImageDesc();
		// R = 3 G = 4 B = 5
		double[] histRImage1 = desc.getHistorgramForJFeature(3, this.image1)
				.getFeatures().get(0);
		double[] histGImage1 = desc.getHistorgramForJFeature(4, this.image1)
				.getFeatures().get(0);
		double[] histBImage1 = desc.getHistorgramForJFeature(5, this.image1)
				.getFeatures().get(0);

		double[] histRImage2 = desc.getHistorgramForJFeature(3, this.image2)
				.getFeatures().get(0);
		double[] histGImage2 = desc.getHistorgramForJFeature(4, this.image2)
				.getFeatures().get(0);
		double[] histBImage2 = desc.getHistorgramForJFeature(5, this.image2)
				.getFeatures().get(0);

		double[] euclidianDistanceArray = new double[histBImage1.length];

		double sumIfItsNANThanEquals =0;
		System.out.println("Euclidian Distance array");
		for (int i = 0; i < histBImage1.length; ++i) {
			euclidianDistanceArray[i] = Math.sqrt(Math.pow(
					(histRImage1[i] - histRImage2[i]), 2)
					+ Math.pow((histGImage1[i] - histGImage2[i]), 2)
					+ Math.pow((histBImage1[i] - histBImage2[i]), 2));
			sumIfItsNANThanEquals+=euclidianDistanceArray[i];
			System.out.print(euclidianDistanceArray[i] + " ");
		}
	
		if(sumIfItsNANThanEquals==0){
			return 0;
		}
		
		System.out.println();
		// normalize a vector
		double[] normalizatedeuclidianDistArray = desc
				.getNormalizatedHistogram(euclidianDistanceArray);
		double distanceNumber=0;
		for (int i = 0; i < histBImage1.length; ++i) {
			distanceNumber+=normalizatedeuclidianDistArray[i];
		}

		return Math.abs(distanceNumber);
	}

}
