package descripteurs;

import java.awt.image.BufferedImage;

import net.semanticmetadata.lire.imageanalysis.JointOpponentHistogram;

public class MyJointOpponentHistogram extends MyDescriptor {


	public MyJointOpponentHistogram(BufferedImage img) {
		super(img);
		lireDescriptor = new JointOpponentHistogram();
	}

	@Override
	public double[] computeHistogram() {
		JointOpponentHistogram eh = (JointOpponentHistogram) lireDescriptor;
		eh.extract(this.getImage());
		return eh.getDoubleHistogram();
	}
}
