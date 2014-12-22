package descripteurs;

import java.awt.image.BufferedImage;

import net.semanticmetadata.lire.imageanalysis.joint.JointHistogram;

/**
 * histogram 64*9 values normalize [0,127]
 * */
public class MyJointHistogram extends MyDescriptor {

	public MyJointHistogram(BufferedImage img) {
		super(img);
		lireDescriptor = new JointHistogram();
	}

	@Override
	public double[] computeHistogram() {
		JointHistogram eh = (JointHistogram) lireDescriptor;
		eh.extract(this.getImage());
		return eh.getDoubleHistogram();
	}

}
