package descripteurs;

import java.awt.image.BufferedImage;

import net.semanticmetadata.lire.imageanalysis.EdgeHistogram;

public class MyEdgeHistogram extends MyDescriptor {

	public MyEdgeHistogram(BufferedImage img) {
		super(img);
		lireDescriptor = new EdgeHistogram();
	}

	@Override
	public double[] computeHistogram() {
		EdgeHistogram eh = (EdgeHistogram) lireDescriptor;
		eh.extract(this.getImage());
		return eh.getDoubleHistogram();
	}

	
}
