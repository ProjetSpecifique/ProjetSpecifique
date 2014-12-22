package descripteurs;

import java.awt.image.BufferedImage;

import net.semanticmetadata.lire.imageanalysis.CEDD;

public class MyCEDD extends MyDescriptor {


	public MyCEDD(BufferedImage img) {
		super(img);
		lireDescriptor = new CEDD();
	}

	@Override
	public double[] computeHistogram() {
		CEDD eh = (CEDD) lireDescriptor;
		eh.extract(this.getImage());
		return eh.getDoubleHistogram();
	}
}
