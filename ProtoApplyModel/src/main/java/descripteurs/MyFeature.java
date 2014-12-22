package descripteurs;

import java.awt.image.BufferedImage;

import net.semanticmetadata.lire.imageanalysis.sift.Feature;

public class MyFeature extends MyDescriptor {


	public MyFeature(BufferedImage img) {
		super(img);
		lireDescriptor = new Feature();
	}

	@Override
	public double[] computeHistogram() {
		Feature eh = (Feature) lireDescriptor;
		eh.extract(this.getImage());
		return eh.getDoubleHistogram();
	}
}
