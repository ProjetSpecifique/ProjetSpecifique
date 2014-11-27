package descripteurs;

import java.awt.image.BufferedImage;

import net.semanticmetadata.lire.imageanalysis.JCD;

public class MyJCD extends MyDescriptor {


	public MyJCD(BufferedImage img) {
		super(img);
		lireDescriptor = new JCD();
	}

	@Override
	public double[] computeHistogram() {
		JCD eh = (JCD) lireDescriptor;
		eh.extract(this.getImage());
		return eh.getDoubleHistogram();
	}
}
