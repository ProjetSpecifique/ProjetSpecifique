package descripteurs;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import com.opencsv.CSVWriter;

import net.semanticmetadata.lire.imageanalysis.AutoColorCorrelogram;
import net.semanticmetadata.lire.utils.FileUtils;
import net.semanticmetadata.lire.utils.ImageUtils;

public class AutoColorCorrelogramDescriptor {

	public static final int MAX_IMAGE_DIMENSION = 1024;

	private static int SIZE = 5;

	// Not used !
	public static void findDistances() {
		String workingDir = System.getProperty("user.dir")
				+ "/data/ferrari/tests/";
		String[] paths = { workingDir + "black.jpg", workingDir + "red.jpg",
				workingDir + "yellow.jpg", workingDir + "white.jpg",
				workingDir + "notferrari.jpg" };
		// Apply descriptor to the photos
		BufferedImage[] bimgs = new BufferedImage[SIZE];
		AutoColorCorrelogram[] accs = new AutoColorCorrelogram[SIZE];
		for (int i = 0; i < SIZE; ++i) {
			bimgs[i] = openImage(paths[i]);
			// Scaling image is especially with the correlogram features very
			// important!
			System.out.println("Size: " + bimgs[i].getHeight() + "x"
					+ bimgs[i].getWidth());
			if (Math.max(bimgs[i].getHeight(), bimgs[i].getWidth()) > MAX_IMAGE_DIMENSION) {
				bimgs[i] = ImageUtils.scaleImage(bimgs[i], MAX_IMAGE_DIMENSION);
			}
			accs[i] = new AutoColorCorrelogram();
			accs[i].extract(bimgs[i]);
		}

		for (int i = 0; i < SIZE; ++i) {
			for (int j = i + 1; j < SIZE; ++j) {
				System.out.print(accs[i].getDistance(accs[j]) + " ");
			}
			System.out.println();
		}
		// System.out.println("Doubles (" + acc.getDoubleHistogram().length +
		// ")");
		// for (double d : acc.getDoubleHistogram()) {
		// System.out.println(d);
		// }
		//
		// System.out.println("String: " + acc.getStringRepresentation());
	}

	public static void computeDescriptor(String workingDir, CSVWriter writer,
			String[] subDirectories) throws IOException {

		// Go through each directory and find all images
		for (String c : subDirectories) {
			ArrayList<File> images = FileUtils.getAllImageFiles(new File(
					workingDir + c + "/"), false);
			// Apply descriptor to the photos
			BufferedImage bimg;
			AutoColorCorrelogram acc = new AutoColorCorrelogram();
			for (File f : images) {
				bimg = ImageIO.read(f);
				// Scaling image is especially with the correlogram features
				// very important!
				if (Math.max(bimg.getHeight(), bimg.getWidth()) > MAX_IMAGE_DIMENSION) {
					bimg = ImageUtils.scaleImage(bimg, MAX_IMAGE_DIMENSION);
				}
				acc.extract(bimg);
				// Get histogram (1024 values in this case).
				double[] histo = acc.getDoubleHistogram();
				String record[] = new String[histo.length + 2];
				record[0] = f.getName(); // photo name, which is also the ID
				for (int i = 0; i < histo.length; ++i) {
					record[i + 1] = Double.toString(histo[i]);
				}
				record[histo.length] = c; // class name
				writer.writeNext(record);

				// String byteVector = "[";
				// for (int k = 0 ; k < acc.getByteArrayRepresentation().length;
				// ++k) {
				// System.out.print(acc.getByteArrayRepresentation()[k] + " ");
				// byteVector += acc.getByteArrayRepresentation()[k] + ",";
				// }
				// byteVector = byteVector.substring(0, byteVector.length()) +
				// "]";
				// writer.writeNext(new String[] {f.getName(), byteVector, c});

			}
		}

		writer.close();
		System.out.println("DONE!");
	}

	public static BufferedImage openImage(String path) {
		BufferedImage result = null;
		try {
			result = ImageIO.read(new FileInputStream(path));
		} catch (Exception e) {
			System.err.println("Couldn't open image with Java, stop. " + path
					+ ", " + e.getMessage());
		}
		return result;
	}
}
