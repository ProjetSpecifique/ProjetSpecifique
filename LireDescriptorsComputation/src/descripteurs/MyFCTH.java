package descripteurs;

import java.awt.image.BufferedImage;

import net.semanticmetadata.lire.imageanalysis.FCTH;

public class MyFCTH extends MyDescriptor {


	public MyFCTH(BufferedImage img) {
		super(img);
		lireDescriptor = new FCTH();
	}

	@Override
	public double[] computeHistogram() {
		FCTH eh = (FCTH) lireDescriptor;
		eh.extract(this.getImage());
		return eh.getDoubleHistogram();
	}
}
