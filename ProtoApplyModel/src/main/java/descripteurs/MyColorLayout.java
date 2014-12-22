package descripteurs;

import java.awt.image.BufferedImage;

import net.semanticmetadata.lire.imageanalysis.BasicFeatures;

public class MyColorLayout extends MyDescriptor {

	public MyColorLayout(BufferedImage img) {
		super(img);
		lireDescriptor = new BasicFeatures();
	}

	@Override
	public double[] computeHistogram() {
		BasicFeatures eh = (BasicFeatures) lireDescriptor;
		eh.extract(this.getImage());
		return eh.getDoubleHistogram();
	}
}
