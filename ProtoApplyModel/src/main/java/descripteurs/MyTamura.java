package descripteurs;

import java.awt.image.BufferedImage;

import net.semanticmetadata.lire.imageanalysis.Tamura;

public class MyTamura extends MyDescriptor {


	public MyTamura(BufferedImage img) {
		super(img);
		lireDescriptor = new Tamura();
	}

	@Override
	public double[] computeHistogram() {
		Tamura eh = (Tamura) lireDescriptor;
		eh.extract(this.getImage());
		return eh.getDoubleHistogram();
	}
}
