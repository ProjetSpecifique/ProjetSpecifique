package descripteurs;

import java.awt.image.BufferedImage;

import net.semanticmetadata.lire.imageanalysis.Gabor;

public class MyGabor extends MyDescriptor {


	public MyGabor(BufferedImage img) {
		super(img);
		lireDescriptor = new Gabor();
	}

	@Override
	public double[] computeHistogram() {
		Gabor eh = (Gabor) lireDescriptor;
		eh.extract(this.getImage());
		return eh.getDoubleHistogram();
	}
}
