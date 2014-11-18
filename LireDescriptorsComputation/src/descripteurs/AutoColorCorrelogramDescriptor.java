package descripteurs;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
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
	
	public static void applyIt() {
		String workingDir = System.getProperty("user.dir") + "/data/ferrari/tests/";
		String[] paths = {
				workingDir + "black.jpg",
				workingDir + "red.jpg",
				workingDir + "yellow.jpg",
				workingDir + "white.jpg",
				workingDir + "notferrari.jpg"
		};
		// Apply descriptor to the photos
		BufferedImage[] bimgs = new BufferedImage[SIZE];
		AutoColorCorrelogram[] accs = new AutoColorCorrelogram[SIZE];
		for (int i = 0; i < SIZE; ++i) {
			bimgs[i] = openImage(paths[i]);
			// Scaling image is especially with the correlogram features very important!
			System.out.println("Size: " + bimgs[i].getHeight() + "x" + bimgs[i].getWidth());
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
//        System.out.println("Doubles (" + acc.getDoubleHistogram().length + ")");
//        for (double d : acc.getDoubleHistogram()) {
//        	System.out.println(d);
//        }
//        
//        System.out.println("String: " + acc.getStringRepresentation());
	}
	
	public static void go2() throws IOException {
		String csv = "data.csv";
	    CSVWriter writer = new CSVWriter(new FileWriter(csv), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
		String workingDir = System.getProperty("user.dir") + "/data/ferrari/";
		String[] colors = { "red", "black", "white", "yellow" }; 
		for (String c : colors) {
			ArrayList<File> images  = FileUtils.getAllImageFiles(new File(workingDir + c + "/"), false);
			// Apply descriptor to the photos
			BufferedImage bimg;
			AutoColorCorrelogram acc = new AutoColorCorrelogram();
			for (File f : images) {
				bimg = ImageIO.read(f);
				// Scaling image is especially with the correlogram features very important!
		        if (Math.max(bimg.getHeight(), bimg.getWidth()) > MAX_IMAGE_DIMENSION) {
		        	bimg = ImageUtils.scaleImage(bimg, MAX_IMAGE_DIMENSION);
		        }
		        acc.extract(bimg);
		        String record[] = new String[1026];
		        double[] histo = acc.getDoubleHistogram();
				record[0] = f.getName();
				for (int i = 0; i < histo.length; ++i) {
					record[i + 1] = Double.toString(histo[i]);
				}
				record[1025] = c; //class name
//		        String byteVector = "[";
//		        for (int k = 0 ; k < acc.getByteArrayRepresentation().length; ++k) {
//		        	System.out.print(acc.getByteArrayRepresentation()[k] + " ");
//		        	byteVector += acc.getByteArrayRepresentation()[k] + ",";
//		        }
//		        byteVector = byteVector.substring(0, byteVector.length()) + "]";
//		        writer.writeNext(new String[] {f.getName(), byteVector, c});
				writer.writeNext(record);
				
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
