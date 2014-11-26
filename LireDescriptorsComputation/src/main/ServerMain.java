package main;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import descripteurs.MyDescriptor;
import descripteurs.MyDescriptorFactory;
import descripteurs.MyDescriptorType;
import net.semanticmetadata.lire.utils.FileUtils;
import utils.*;

/**
 * Super Main
 * 
 * @author AdeN
 *
 */
public class ServerMain {

	private static MyDescriptor myDescriptor;

	/**
	 * args[0] - path images args[1] - path CSVs
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		File imgDir;
		File csvDir;

		if (args.length != 2) {
			throw new Exception("Wrong number of arguments");
		}

		if (args[0] == null) {
			throw new Exception("arg 0 cannot be null");
		}

		if (args[1] == null) {
			throw new Exception("arg 1 cannot be null");
		}

		imgDir = new File(args[0]);
		csvDir = new File(args[1]);

		if (!imgDir.exists() || !imgDir.isAbsolute()) {
			throw new Exception("Specified csv path doesn't exist or is not a directory");
		}

		if (!csvDir.exists() || !csvDir.isAbsolute()) {
			throw new Exception("Specified csv path doesn't exist or is not a directory");
		}

		/* compute and print the csv for all implemented descriptors */
		System.out.println("---- Start computations ----");
		for (MyDescriptorType descriptorType : MyDescriptorType.values()) {

			System.out.println("Start	: " + descriptorType.name());
			try {
				extractHistogramsForDescriptor(descriptorType, imgDir, csvDir);
			} catch (Exception e) {
				System.err.println(e);
			}
			System.out.println("End 	: " + descriptorType.name() + "\n");
		}
		System.out.println("---- End computations ----");
	}

	private static void extractHistogramsForDescriptor(MyDescriptorType descriptorType, File imgDir, File csvDir)
			throws Exception {
		myDescriptor = MyDescriptorFactory.buildDescriptor(descriptorType, null);
		File imgFile;
		BufferedImage bufferedImgX;
		// csvName = descriptorType.name()
		WriteCSV.initWriter(csvDir, descriptorType.name());

		ArrayList<File> allImages = FileUtils.getAllImageFiles(imgDir, true);

		for (Iterator<File> i = allImages.iterator(); i.hasNext();) {
			imgFile = i.next();
			bufferedImgX = ImageUtils.openImage(imgFile);

			myDescriptor.setImage(bufferedImgX);
			WriteCSV.writeHistogramLine(imgFile.getName(), myDescriptor.computeHistogram());
		}
	}
}
