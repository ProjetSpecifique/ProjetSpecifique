package descripteurs;

import java.awt.image.BufferedImage;

import net.semanticmetadata.lire.imageanalysis.SurfFeature;

public class MyMSERFeature extends MyDescriptor {


	public MyMSERFeature(BufferedImage img) {
		super(img);
		lireDescriptor = new SurfFeature();
	}

	@Override
	public double[] computeHistogram() {
		SurfFeature eh = (SurfFeature) lireDescriptor;
		eh.extract(this.getImage());
		return eh.getDoubleHistogram();
	}
}
