package descripteurs;

import java.awt.image.BufferedImage;

import net.semanticmetadata.lire.imageanalysis.ScalableColor;

public class MyScalableColor extends MyDescriptor {


	public MyScalableColor(BufferedImage img) {
		super(img);
		lireDescriptor = new ScalableColor();
	}

	@Override
	public double[] computeHistogram() {
		ScalableColor eh = (ScalableColor) lireDescriptor;
		eh.extract(this.getImage());
		return eh.getDoubleHistogram();
	}
}
