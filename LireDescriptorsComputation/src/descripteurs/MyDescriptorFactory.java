package descripteurs;

import java.awt.image.BufferedImage;

public class MyDescriptorFactory {

	public static MyDescriptor buildDescriptor (MyDescriptorType descriptorType, BufferedImage bimg) {
				
		switch (descriptorType) {
		case AutoColorCorrelogram:
			return new MyAutoColorCorrelogram(bimg);
		case BasicFeatures:
			return new MyBasicFeatures(bimg);
		case CEDD:
			return new MyCEDD(bimg);
		case ColorLayout:
			return new MyColorLayout(bimg);
		case EdgeHistogram:
			return new MyEdgeHistogram(bimg);
		case FCTH:
			return new MyFCTH(bimg);
		case Feature:
			return new MyFeature(bimg);
		case FuzzyColorHistogram:
			return new MyFuzzyColorHistogram(bimg);
		case FuzzyOpponentHistogram:
			return new MyFuzzyOpponentHistogram(bimg);
		case Gabor:
			return new MyGabor(bimg);
		case JCD:
			return new MyJCD(bimg);
		case JointHistogram:
			return new MyJointHistogram(bimg);
		case JointOpponentHistogram:
			return new MyJointOpponentHistogram(bimg);
		case JpegCoefficientHistogram:
			return new MyJpegCoefficientHistogram(bimg);
		case MSERFeature:
			return new MyMSERFeature(bimg);
		case OpponentHistogram:
			return new MyOpponentHistogram(bimg);
		case PHOG:
			return new MyPHOG(bimg);
		case ScalableColor:
			return new MyScalableColor(bimg);
		case SimpleColorHistogram:
			return new MySimpleColorHistogram(bimg);
		case SurfFeature:
			return new MySurfFeature(bimg);
		case Tamura:
			return new MyTamura(bimg);

		default:
			return null;
		}
	}
}
