package descripteurs;

import java.awt.image.BufferedImage;

public abstract class MyDescriptor {

	private BufferedImage image;
	protected Object lireDescriptor;

	/*
	 * All LIRE descriptors implement LireFeature interface which defines
	 * the extract method
	 */
	public MyDescriptor(BufferedImage img) {
		this.image = img;
	}

	/**
	 * Computes the descriptor's histogram and returns it as an array of double
	 * 
	 * PS: All LIRE descriptors implement LireFeature interface which offers 2
	 * getters for the result
	 * 
	 * 1. getByteArrayRepresentation() : returns an array of bytes, but
	 * sometimes stuffs 2 numbers into one byte
	 * 
	 * 2.getDoubleHistogram() : stupid name, there are not 2 histograms but a
	 * histogram of doubles; some descriptors return integer values and they are
	 * converted into double using ConversionUtils
	 * */
	public abstract double[] computeHistogram();

	/* getter image */
	public BufferedImage getImage() {
		return image;
	}

	/* setter image */
	public void setImage(BufferedImage image) {
		this.image = image;
	}

}
