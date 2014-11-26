package descripteurs;

import java.awt.image.BufferedImage;

import net.semanticmetadata.lire.imageanalysis.PHOG;

public class MyPHOG extends MyDescriptor {


	public MyPHOG(BufferedImage img) {
		super(img);
		lireDescriptor = new PHOG();
	}

	@Override
	public double[] computeHistogram() {
		PHOG eh = (PHOG) lireDescriptor;
		eh.extract(this.getImage());
		return eh.getDoubleHistogram();
	}
}
