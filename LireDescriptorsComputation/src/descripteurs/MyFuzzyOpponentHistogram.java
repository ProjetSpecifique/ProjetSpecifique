package descripteurs;

import java.awt.image.BufferedImage;

import net.semanticmetadata.lire.imageanalysis.FuzzyOpponentHistogram;

public class MyFuzzyOpponentHistogram extends MyDescriptor {


	public MyFuzzyOpponentHistogram(BufferedImage img) {
		super(img);
		lireDescriptor = new FuzzyOpponentHistogram();
	}

	@Override
	public double[] computeHistogram() {
		FuzzyOpponentHistogram eh = (FuzzyOpponentHistogram) lireDescriptor;
		eh.extract(this.getImage());
		return eh.getDoubleHistogram();
	}
}
