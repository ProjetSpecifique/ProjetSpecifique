package descripteurs;

import java.awt.image.BufferedImage;

import net.semanticmetadata.lire.imageanalysis.FuzzyColorHistogram;

public class MyFuzzyColorHistogram extends MyDescriptor {


	public MyFuzzyColorHistogram(BufferedImage img) {
		super(img);
		lireDescriptor = new FuzzyColorHistogram();
	}

	@Override
	public double[] computeHistogram() {
		FuzzyColorHistogram eh = (FuzzyColorHistogram) lireDescriptor;
		eh.extract(this.getImage());
		return eh.getDoubleHistogram();
	}
}
