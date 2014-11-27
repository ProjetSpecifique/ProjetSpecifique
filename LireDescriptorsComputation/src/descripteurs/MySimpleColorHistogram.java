package descripteurs;

import java.awt.image.BufferedImage;

import net.semanticmetadata.lire.imageanalysis.SimpleColorHistogram;

public class MySimpleColorHistogram extends MyDescriptor {

	public MySimpleColorHistogram(BufferedImage img) {
		super(img);
		lireDescriptor = new SimpleColorHistogram();
	}

	@Override
	public double[] computeHistogram() {
		SimpleColorHistogram eh = (SimpleColorHistogram) lireDescriptor;
		eh.extract(this.getImage());
		return eh.getDoubleHistogram();
	}
}
