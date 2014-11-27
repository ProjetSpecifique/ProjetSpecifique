package utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

import javax.imageio.ImageIO;

public class ImageUtils {
	
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
	
	
	public static BufferedImage openImage(File imgFile) {
		BufferedImage result = null;
		try {
			result = ImageIO.read(new FileInputStream(imgFile));
		} catch (Exception e) {
			System.err.println("Couldn't open image with Java, stop. " + imgFile.getAbsolutePath()
					+ ", " + e.getMessage());
		}
		return result;
	}

	
}
