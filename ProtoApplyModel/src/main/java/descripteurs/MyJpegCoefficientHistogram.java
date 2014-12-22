package descripteurs;

import java.awt.image.BufferedImage;

import net.semanticmetadata.lire.imageanalysis.JpegCoefficientHistogram;

public class MyJpegCoefficientHistogram extends MyDescriptor {

	public MyJpegCoefficientHistogram(BufferedImage img) {
		super(img);
		lireDescriptor = new JpegCoefficientHistogram();
	}

	@Override
	public double[] computeHistogram() {
		JpegCoefficientHistogram eh = (JpegCoefficientHistogram) lireDescriptor;
		eh.extract(this.getImage());
		return eh.getDoubleHistogram();
	}
}
