package ImageCompV;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.imageio.ImageIO;

import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.Transforms;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.FaceDetector;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;

import com.labun.surf.InterestPoint;
import com.labun.surf.Matcher;

import de.lmu.ifi.dbs.jfeaturelib.LibProperties;
import de.lmu.ifi.dbs.jfeaturelib.edgeDetector.Susan;
import de.lmu.ifi.dbs.jfeaturelib.features.Histogram;
import de.lmu.ifi.dbs.utilities.Arrays2;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.io.Opener;
import ij.process.Blitter;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;

/**
 * @author Huni
 * 
 */
public class ImageDesc {

	String path1;
	String path2;

	public ImageDesc() {
	}

	public ImageDesc(String path1, String path2) {
		this.path1 = path1;
		this.path2 = path2;
	}

	// writes out on the console the entropy value of each image( two images
	// loaded in the constuctor)
	public void theTwoImageEntorpyValue() {
		System.out.println();
		System.out.println("\nThe first image Entropy Value: "
				+ imageSegmentation(this.path1)
				+ " second image Entropy Value: "
				+ imageSegmentation(this.path2));
	}

	// work like the function above if 0 is that they arre equal
	public double theTwoImageEntorpyValueDistance(String path1, String path2) {
		return Math.abs(imageSegmentation(path1)-imageSegmentation(path2));
	}

	// params: path of the image
	// return the Entropy Value of 8 level gray scale
	public double imageSegmentation(String path) {
		int thresholdLevel = 8;
		ArrayList<double[]> listOfEntropyHistogram = new ArrayList<double[]>();

		for (int i = 1; i <= thresholdLevel; ++i) {
			listOfEntropyHistogram.add(entropyMeasureHistogram(i, path));
		}

		double[] entropyValueArray = new double[256];

		// System.out.println("Size of listOne "+listOfEntropyHistogram.size());
		for (int i = 0; i < listOfEntropyHistogram.size(); ++i) {
			for (int j = 0; j < 256; ++j) {
				entropyValueArray[j] += listOfEntropyHistogram.get(i)[j];
			}
		}

		// System.out.println("EntropyValueArray");
		double theEntropyValue = 0;
		for (double element : entropyValueArray) {
			if (!Double.isNaN(element)) {
				// System.out.print((0-element)+" ");
				theEntropyValue += element;
			}
		}
		theEntropyValue = 0 - theEntropyValue;
		// System.out.println("\nThe Entropy Value "+theEntropyValue);
		return theEntropyValue;
	}

	// simple distance of two image histogram
	// parameter type 0 gray...
	public double simpleDistanceHistogram(int histType) throws IOException {
		File f = new File(this.path1);
		ColorProcessor image = new ColorProcessor(ImageIO.read(f));
		File f2 = new File(this.path2);
		ColorProcessor image2 = new ColorProcessor(ImageIO.read(f2));

		List<double[]> features = getHistorgramForJFeature(histType, image)
				.getFeatures();
		double[] histogramFirstImage = features.get(0);

		features = getHistorgramForJFeature(histType, image2).getFeatures();

		double[] histogramSecondImage = features.get(0);

		double[] tempDifferences = new double[256];

		double difference = 0;

		System.out.println("Array:");
		for (int i = 0; i < histogramFirstImage.length; ++i) {
			tempDifferences[i] = Math.pow(
					(histogramFirstImage[i] - histogramSecondImage[i]), 2)
					/ histogramFirstImage[i];
			difference += tempDifferences[i];
			System.out.print(tempDifferences[i] + " ");
		}
		System.out.println();

		return difference;
	}

	// Entropy is a measure of histogram uniformity. The closer to the uniform
	// distrbution the higher the returnned value
	public double[] entropyMeasureHistogram(int level, String path) {
		Segmentize segmentize = new Segmentize(loadImage(path), level);
		BufferedImage dstImage = segmentize.segmentize();
		ImagePlus imagePlus = new ImagePlus("First Image", dstImage);
		ColorProcessor colorProcessor = new ColorProcessor(imagePlus.getImage());
		double totalNumberOfPixels = colorProcessor.getHeight()
				* colorProcessor.getWidth();
		double[] grayHistorgamImage = getHistorgramForJFeature(0,
				colorProcessor).getFeatures().get(0);
		double[] probabilityHist = new double[grayHistorgamImage.length];
		for (int i = 0; i < grayHistorgamImage.length; ++i) {
			probabilityHist[i] = ((grayHistorgamImage[i] / totalNumberOfPixels) * (Math
					.log10(grayHistorgamImage[i] / totalNumberOfPixels) / Math
					.log10(2)));

		}
		return probabilityHist;
	}

	// return a double number from 0 - ... , if its 0 the two pictures are the
	// same
	public double colorQuantizationEuclidianDistance() throws IOException {
		File f = new File(this.path1);
		ColorProcessor image1 = new ColorProcessor(ImageIO.read(f));
		File f2 = new File(this.path2);
		ColorProcessor image2 = new ColorProcessor(ImageIO.read(f2));
		ColorQuantization colorQuantization = new ColorQuantization(image1,
				image2);
		return colorQuantization.getColorQuantizationEuclidianDistance();
	}

	// params: image, next 2 : start positions, with of rectangle, height of
	// rectangle, type of histogram
	// return the separeted image histogram
	public double[] getHistOfaRoidImage(ColorProcessor image, double posx,
			double posy, double width, double height, int typeHist)
			throws IOException {
		Roi roi = new Roi((int) posx, (int) posy, (int) width, (int) height);
		image.setRoi(roi);

		List<double[]> features = getHistorgramForJFeature(typeHist, image)
				.getFeatures();

		return features.get(0);

	}

	public double[] getHistogramByType(String path,int histType) throws IOException{
		File f = new File(path);
		ColorProcessor image = new ColorProcessor(ImageIO.read(f));
		List<double[]> features = getHistorgramForJFeature(histType, image)
				.getFeatures();
		double[] histogram = features.get(0);
		return histogram;
	}
	
	// params: histogramType(Gray=0 , HSB(hue saturation brightness) = 1,Blue =
	// 2, Green.= 3, Red= 4 , RGB= 5),
	// return a double ,0 the two images are identical, 0-50 similar , 100>0
	// different
	// and writes out similar interest points-not now
	public double normalizationOfTwoSameTypeOfHistogram(int histType,
			String path1, String path2) throws IOException {

		// read the two images
		File f = new File(path1);
		ColorProcessor image = new ColorProcessor(ImageIO.read(f));
		File f2 = new File(path2);
		ColorProcessor image2 = new ColorProcessor(ImageIO.read(f2));

		List<double[]> features = getHistorgramForJFeature(histType, image)
				.getFeatures();
		double[] histogramFirstImage = features.get(0);

		features = getHistorgramForJFeature(histType, image2).getFeatures();

		double[] histogramSecondImage = features.get(0);

		double[] normalizatedHist1 = getNormalizatedHistogram(histogramFirstImage);
		double[] normalizatedHist2 = getNormalizatedHistogram(histogramSecondImage);

		double differenceBetweenHists = 0;
		double[] differenceHistVect = new double[256];
		// System.out.println("Normal Same Type hist");
		double max = 0;
		for (int i = 0; i < normalizatedHist1.length; ++i) {
			// System.out.print(normalizatedHist2[i]+" ");
			/*
			 * differenceBetweenHists += (Math.abs(normalizatedHist1[i]) - Math
			 * .abs(normalizatedHist2[i]));
			 */
			differenceHistVect[i] = (Math.abs(normalizatedHist1[i]) - Math
					.abs(normalizatedHist2[i]));
			if (((Math.abs(normalizatedHist1[i]) - Math
					.abs(normalizatedHist2[i]))) > max) {
				max = (Math.abs(normalizatedHist1[i]) - Math
						.abs(normalizatedHist2[i]));
			}

		}

		for (int i = 0; i < differenceHistVect.length; ++i) {
			differenceBetweenHists += (differenceHistVect[i] / max);
		}
		// System.out.println();

		/*
		 * System.out.println("Number of Commun Interest Points: " +
		 * get2ImageCommunInterestPoint());
		 */

		return Math.abs(differenceBetweenHists);
	}
	//difference that not path is given, only histogram arrray
	public double normalizationOfTwoSameTypeOfHistogram(int histType,
			double[] histogramFirstImage, double[] histogramSecondImage) throws IOException {

		double[] normalizatedHist1 = getNormalizatedHistogram(histogramFirstImage);
		double[] normalizatedHist2 = getNormalizatedHistogram(histogramSecondImage);

		double differenceBetweenHists = 0;
		double[] differenceHistVect = new double[256];
		// System.out.println("Normal Same Type hist");
		double max = 0;
		for (int i = 0; i < normalizatedHist1.length; ++i) {
			// System.out.print(normalizatedHist2[i]+" ");
			/*
			 * differenceBetweenHists += (Math.abs(normalizatedHist1[i]) - Math
			 * .abs(normalizatedHist2[i]));
			 */
			differenceHistVect[i] = (Math.abs(normalizatedHist1[i]) - Math
					.abs(normalizatedHist2[i]));
			if (((Math.abs(normalizatedHist1[i]) - Math
					.abs(normalizatedHist2[i]))) > max) {
				max = (Math.abs(normalizatedHist1[i]) - Math
						.abs(normalizatedHist2[i]));
			}

		}

		for (int i = 0; i < differenceHistVect.length; ++i) {
			differenceBetweenHists += (differenceHistVect[i] / max);
		}
		// System.out.println();

		/*
		 * System.out.println("Number of Commun Interest Points: " +
		 * get2ImageCommunInterestPoint());
		 */

		return Math.abs(differenceBetweenHists);
	}

	public Double[] getNormHistogramForOneImage(String path,int typeHist) throws IOException{
		double[] hist;
		
		File f = new File(path);
		ColorProcessor image = new ColorProcessor(ImageIO.read(f));
		List<double[]> features = getHistorgramForJFeature(typeHist, image)
				.getFeatures();
		hist = features.get(0);
		double[] normalizatedHist = getNormalizatedHistogram(hist);
		
		Double[] nHist =new Double[256];
		for(int i=0;i<nHist.length;++i){
			nHist[i]=normalizatedHist[i];
		}
			
		return nHist;
	}
	
	// params: histogramType(Gray=0 , HSB(hue saturation brightness) = 1,Blue =
	// 2, Green.= 3, Red= 4 , RGB= 5), and number of parts
	// return a double ,0 the two images are identical, 0-50 similar , 100>0
	// different
	// and writes out similar interest points
	public double normalizationOfTwoSameTypeOfHistogramAndDividThem(
			int histType, int devideNumber) throws IOException {
		// read the two images
		File f = new File(this.path1);
		ColorProcessor image = new ColorProcessor(ImageIO.read(f));
		File f2 = new File(this.path2);
		ColorProcessor image2 = new ColorProcessor(ImageIO.read(f2));
		int partx = 0;
		List<double[]> normalizatedHists1 = new ArrayList<double[]>();

		List<double[]> normalizatedHists2 = new ArrayList<double[]>();

		while ((partx + (image.getWidth() / devideNumber)) <= image.getWidth()) {
			double[] oneHist = getHistOfaRoidImage(image, partx, 0,
					(image.getWidth() / devideNumber), image.getHeight(),
					histType);
			normalizatedHists1.add(getNormalizatedHistogram(oneHist));
			partx += (image.getWidth() / devideNumber);
		}

		partx = 0;
		while ((partx + (image2.getWidth() / devideNumber)) <= image2
				.getWidth()) {
			double[] oneHist = getHistOfaRoidImage(image2, partx, 0,
					(image2.getWidth() / devideNumber), image2.getHeight(),
					histType);
			normalizatedHists2.add(getNormalizatedHistogram(oneHist));
			partx += (image2.getWidth() / devideNumber);
		}

		double differenceBetweenHists = 0;

		// System.out.println("First pic at normHistDiffRoiDivImage");
		// for(int i=0;i<normalizatedHists1.size();++i){
		// for(int j=0;j<normalizatedHists1.get(i).length;++j){
		// System.out.print(normalizatedHists1.get(i)[j]+" ");
		// }
		// System.out.println();
		// }
		//
		// System.out.println("Second pic at normHistDiffRoiDivImage");
		// for(int i=0;i<normalizatedHists2.size();++i){
		// for(int j=0;j<normalizatedHists2.get(i).length;++j){
		// System.out.print(normalizatedHists2.get(i)[j]+" ");
		// }
		// System.out.println();
		// }

		List<Double> differenesByParts = new ArrayList<Double>();

		for (int k = 0; k < normalizatedHists1.size(); ++k) {
			for (int i = 0; i < normalizatedHists1.get(k).length; ++i) {
				differenceBetweenHists += (Math
						.abs(normalizatedHists1.get(k)[i]) - Math
						.abs(normalizatedHists2.get(k)[i]));
			}
			differenesByParts.add(Math.abs(differenceBetweenHists));
			differenceBetweenHists = 0;
		}
		System.out.println();
		System.out.println("Differenc By Parts Divide Pics");
		for (double d : differenesByParts) {
			System.out.print(d + " ");
		}
		System.out.println();

		return -1;// Math.abs(differenceBetweenHists);
	}

	// ezen meg kiserletezek
	public double comparationWithStatistics() throws IOException {

		File f = new File(this.path1);
		ColorProcessor image = new ColorProcessor(ImageIO.read(f));
		List<double[]> features = getHistorgramForJFeature(0, image)
				.getFeatures();
		double[] histogramFirstImage = features.get(0);

		double[] probability = new double[256];
		int index = 0;
		double sum = 0;
		for (double d : histogramFirstImage) {
			probability[index] = d / (image.getHeight() * image.getWidth());
			sum += probability[index];
			++index;
		}

		for (int i = 0; i < probability.length; ++i) {
			System.out.print(probability[i] + " ");
		}
		System.out.println();
		System.out.println("sum " + sum);
		System.out.println("sumpixels " + image.getHeight() * image.getWidth());

		return -1;
	}

	// params: histogram double*
	// returns histogram double* normalizated
	public double[] getNormalizatedHistogram(double[] histogram) {
		double xk = 0;
		double N = histogram.length;
		for (double d : histogram) {
			xk += d;
		}

		xk = (1 / N) * xk;

		double qk = 0;

		for (double d : histogram) {
			qk += (d - xk) * (d - xk);
		}

		qk = (1 / (N - 1)) * qk;
		qk = Math.sqrt(qk);

		// double sum = 0;

		// System.out.println("Normalization vector:(histogram)");
		for (int i = 0; i < histogram.length; ++i) {
			histogram[i] = (histogram[i] - xk) / qk;
			// sum += (histogram[i] - xk) / qk;
			// System.out.print(histogram[i] + " ");
		}
		// System.out.println();
		// System.out.println("Sum " + sum);

		return histogram;
	}

	public int get2ImageCommunInterestPoint(String path1, String path2) {
		List<InterestPoint> firstImageKeyPoints = Matcher
				.extractKeypoints(path1);

		List<InterestPoint> secondImageKeyPoints = Matcher
				.extractKeypoints(path2);

		Map<InterestPoint, InterestPoint> commonKeypoints = Matcher.findMathes(
				firstImageKeyPoints, secondImageKeyPoints, 0.5f, true);

		return commonKeypoints.size();
	}

	// ImageJ library : just a simple hist 0-1 returned number
	public double imageJHistogram() {
		try {
			Opener opener = new Opener();
			ImagePlus imp = opener.openImage(this.path1);
			ImageProcessor ip = imp.getProcessor();

			Opener opener2 = new Opener();
			ImagePlus imp2 = opener2.openImage(this.path2);
			ImageProcessor ip2 = imp2.getProcessor();

			ip.copyBits(ip, 0, 0, Blitter.COPY);
			int[] hist = (int[]) ip.getHistogram();

			ip2.copyBits(ip2, 0, 0, Blitter.COPY);
			int[] hist2 = (int[]) ip2.getHistogram();

			/*
			 * System.out.println("Elso kep hist" +hist.length); for (int i = 0;
			 * i < hist.length; ++i) { System.out.print(hist[i] + " "); }
			 * System.out.println();
			 * 
			 * System.out.println("Masodik kep hist "+hist2.length); for (int i
			 * = 0; i < hist2.length; ++i) { System.out.print(hist2[i] + " "); }
			 * System.out.println();
			 */

			double help = 0;
			for (int i = 0; i < hist2.length; ++i) {
				if (Math.abs(hist[i] - hist2[i]) <= 1000) {
					++help;
				}
			}
			help = (1 + help) / hist.length;
			// System.out.println("help "+help);
			return help;

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		return 0;
	}

	// openImaj-not working good
	// params path of the image
	// return number of faces detected
	public int getNumberOfFaceisOpenImaj(String path)
			throws FileNotFoundException, IOException {

		MBFImage openimagej = ImageUtilities.readMBF(new FileInputStream(path));
		// DisplayUtilities.display(openimagej);
		FaceDetector<DetectedFace, FImage> fd = new HaarCascadeDetector(40);
		List<DetectedFace> faces = fd.detectFaces(Transforms
				.calculateIntensity(openimagej));
		return faces.size();
	}
	
	public double getNumberOfFacesOnTwoImage(String path1, String path2){
		try {
			return Math.abs(getNumberOfFaceisOpenImaj(path1)-getNumberOfFaceisOpenImaj(path2));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1024;
	}

	// JfeatureLib : rgb, r,g,b, gray,hsb
	public void jfeatureLibHistogramsRGB() throws IOException {

		File f = new File(this.path1);
		ColorProcessor image = new ColorProcessor(ImageIO.read(f));
		File f2 = new File(this.path2);
		ColorProcessor image2 = new ColorProcessor(ImageIO.read(f2));

		List<double[]> features = getHistorgramForJFeature(5, image)
				.getFeatures();
		System.out.println("Image RGB hisztogram01");
		for (double[] feature : features) {
			System.out.println(Arrays2.join(feature, ", ", "%.5f"));
		}

		List<double[]> features2 = getHistorgramForJFeature(5, image2)
				.getFeatures();
		System.out.println("Image RGB hisztogram02");
		for (double[] feature : features2) {
			System.out.println(Arrays2.join(feature, ", ", "%.5f"));
		}

	}

	public void jfeatureLibHistogramsRED() throws IOException {
		File f = new File(this.path1);
		ColorProcessor image = new ColorProcessor(ImageIO.read(f));
		File f2 = new File(this.path2);
		ColorProcessor image2 = new ColorProcessor(ImageIO.read(f2));

		List<double[]> features = getHistorgramForJFeature(4, image)
				.getFeatures();
		System.out.println("Image RED hisztogram01");
		for (double[] feature : features) {
			System.out.println(Arrays2.join(feature, ", ", "%.5f"));
		}

		List<double[]> features2 = getHistorgramForJFeature(4, image2)
				.getFeatures();
		System.out.println("Image RED hisztogram02");
		for (double[] feature : features2) {
			System.out.println(Arrays2.join(feature, ", ", "%.5f"));
		}
	}

	public void jfeatureLibHistogramsGREEN() throws IOException {
		File f = new File(this.path1);
		ColorProcessor image = new ColorProcessor(ImageIO.read(f));
		File f2 = new File(this.path2);
		ColorProcessor image2 = new ColorProcessor(ImageIO.read(f2));

		List<double[]> features = getHistorgramForJFeature(3, image)
				.getFeatures();
		System.out.println("Image GREEN hisztogram01");
		for (double[] feature : features) {
			System.out.println(Arrays2.join(feature, ", ", "%.5f"));
		}

		List<double[]> features2 = getHistorgramForJFeature(3, image2)
				.getFeatures();
		System.out.println("Image GREEN hisztogram02");
		for (double[] feature : features2) {
			System.out.println(Arrays2.join(feature, ", ", "%.5f"));
		}
	}

	public void jfeatureLibHistogramsBLUE() throws IOException {
		File f = new File(this.path1);
		ColorProcessor image = new ColorProcessor(ImageIO.read(f));
		File f2 = new File(this.path2);
		ColorProcessor image2 = new ColorProcessor(ImageIO.read(f2));

		List<double[]> features = getHistorgramForJFeature(2, image)
				.getFeatures();
		System.out.println("Image BLUE hisztogram01");
		for (double[] feature : features) {
			System.out.println(Arrays2.join(feature, ", ", "%.5f"));
		}

		List<double[]> features2 = getHistorgramForJFeature(2, image2)
				.getFeatures();
		System.out.println("Image BLUE hisztogram02");
		for (double[] feature : features2) {
			System.out.println(Arrays2.join(feature, ", ", "%.5f"));
		}
	}

	public void jfeatureLibHistogramsHSB() throws IOException {
		File f = new File(this.path1);
		ColorProcessor image = new ColorProcessor(ImageIO.read(f));
		File f2 = new File(this.path2);
		ColorProcessor image2 = new ColorProcessor(ImageIO.read(f2));

		List<double[]> features = getHistorgramForJFeature(1, image)
				.getFeatures();
		System.out.println("Image HSB hisztogram01");
		for (double[] feature : features) {
			System.out.println(Arrays2.join(feature, ", ", "%.5f"));
		}

		List<double[]> features2 = getHistorgramForJFeature(1, image2)
				.getFeatures();
		System.out.println("Image HSB hisztogram02");
		for (double[] feature : features2) {
			System.out.println(Arrays2.join(feature, ", ", "%.5f"));
		}
	}

	public void jfeatureLibHistogramsGRAY() throws IOException {
		File f = new File(this.path1);
		ColorProcessor image = new ColorProcessor(ImageIO.read(f));
		File f2 = new File(this.path2);
		ColorProcessor image2 = new ColorProcessor(ImageIO.read(f2));

		List<double[]> features = getHistorgramForJFeature(0, image)
				.getFeatures();
		System.out.println("Image GRAY hisztogram01");
		for (double[] feature : features) {
			System.out.println(Arrays2.join(feature, ", ", "%.5f"));
		}

		List<double[]> features2 = getHistorgramForJFeature(0, image2)
				.getFeatures();
		System.out.println("Image GRAY hisztogram02");
		for (double[] feature : features2) {
			System.out.println(Arrays2.join(feature, ", ", "%.5f"));
		}
	}

	// params:
	// first Histogram Types: Gray=0 , HSB(hue saturation brightness) = 1,Blue =
	// 2, Green.= 3, Red= 4 , RGB= 5
	// second is the image colorprocessor
	// return a Histogram from jfeaturelib-library
	public Histogram getHistorgramForJFeature(int type, ColorProcessor img) {
		LibProperties properties;
		try {
			properties = LibProperties.get();
			switch (type) {
			case 0:
				properties.setProperty(LibProperties.HISTOGRAMS_TYPE,
						Histogram.TYPE.Gray.name());
				break;
			case 1:
				properties.setProperty(LibProperties.HISTOGRAMS_TYPE,
						Histogram.TYPE.HSB.name());
				break;
			case 2:
				properties.setProperty(LibProperties.HISTOGRAMS_TYPE,
						Histogram.TYPE.Blue.name());
				break;
			case 3:
				properties.setProperty(LibProperties.HISTOGRAMS_TYPE,
						Histogram.TYPE.Green.name());
				break;
			case 4:
				properties.setProperty(LibProperties.HISTOGRAMS_TYPE,
						Histogram.TYPE.Red.name());
				break;
			case 5:
				properties.setProperty(LibProperties.HISTOGRAMS_TYPE,
						Histogram.TYPE.RGB.name());
				break;
			case 6:
				properties.setProperty(LibProperties.HISTOGRAMS_TYPE,
						Histogram.TYPE.Brightness.name());
				break;
			default:
				break;
			}
			Histogram descript = new Histogram();
			descript.setProperties(properties);
			descript.run(img);
			return descript;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// params: path of the image
	// returns buffer of it
	public static BufferedImage loadImage(String filename) {
		BufferedImage result = null;
		try {
			result = ImageIO.read(new File(filename));
		} catch (Exception e) {
			System.out.println(e.toString() + " Image '" + filename
					+ "' not found.");
		}
		return result;
	}

	// Openimage edge detector
	public void openImageEdgeDetect() {
		Susan s = new Susan();
		ImagePlus imagePlus = new ImagePlus("Pic", loadImage(this.path1));
		ImageProcessor iproc = imagePlus.getProcessor();
	}

}
