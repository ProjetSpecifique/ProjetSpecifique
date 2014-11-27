package descripteurs;

public enum MyDescriptorType {
	AutoColorCorrelogram    ,
	BasicFeatures           ,
	CEDD                    ,
	ColorLayout             ,
	EdgeHistogram           ,
	FCTH                    ,
	FuzzyColorHistogram     ,
	FuzzyOpponentHistogram  ,
	Gabor                   ,
	JCD                     ,
	ScalableColor           ,
	SimpleColorHistogram    ,
	JointHistogram          ,
	Feature                 ,
	// OpponentHistogram       , extraction method not implemented in LIRE
	// JointOpponentHistogram  , extraction method not implemented in LIRE
	SurfFeature             ,  // java.lang.UnsupportedOperationException: No implemented!
	MSERFeature             , // java.lang.NullPointerException
	JpegCoefficientHistogram,
	Tamura                  ,
	PHOG                    ;
}
