package descripteurs;

import java.awt.image.BufferedImage;

import net.semanticmetadata.lire.imageanalysis.AutoColorCorrelogram;

public class MyAutoColorCorrelogram extends MyDescriptor {


	public MyAutoColorCorrelogram(BufferedImage img) {
		super(img);
		lireDescriptor = new AutoColorCorrelogram();
	}

	@Override
	public double[] computeHistogram() {
		AutoColorCorrelogram eh = (AutoColorCorrelogram) lireDescriptor;
		eh.extract(this.getImage());
		return eh.getDoubleHistogram();
	}
}
