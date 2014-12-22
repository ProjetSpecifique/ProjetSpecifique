package descripteurs;

import java.awt.image.BufferedImage;

import net.semanticmetadata.lire.imageanalysis.OpponentHistogram;

public class MyOpponentHistogram extends MyDescriptor {


	public MyOpponentHistogram(BufferedImage img) {
		super(img);
		lireDescriptor = new OpponentHistogram();
	}

	@Override
	public double[] computeHistogram() {
		OpponentHistogram eh = (OpponentHistogram) lireDescriptor;
		eh.extract(this.getImage());
		return eh.getDoubleHistogram();
	}
}
