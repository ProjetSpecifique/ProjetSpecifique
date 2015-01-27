package main;

import descripteurs.MyDescriptorType;
import evaluators.MyLearnerType;

public enum MyModel {

	CoastLine1(MyTerm.coastline, MyDescriptorType.AutoColorCorrelogram, MyLearnerType.svm),
	CoastLine2(MyTerm.coastline, MyDescriptorType.CEDD, MyLearnerType.svm),
	CoastLine3(MyTerm.coastline, MyDescriptorType.PHOG, MyLearnerType.svm),
	Water1(MyTerm.water, MyDescriptorType.AutoColorCorrelogram, MyLearnerType.svm),
	Water2(MyTerm.water, MyDescriptorType.PHOG, MyLearnerType.rProp),
	Water3(MyTerm.water, MyDescriptorType.PHOG, MyLearnerType.svm),
	Desert(MyTerm.desert, MyDescriptorType.AutoColorCorrelogram, MyLearnerType.svm),
	Dune(MyTerm.dune, MyDescriptorType.AutoColorCorrelogram, MyLearnerType.svm),
	Glacier(MyTerm.glacier, MyDescriptorType.PHOG, MyLearnerType.svm),
	Island(MyTerm.island, MyDescriptorType.PHOG, MyLearnerType.svm),
	Nature1(MyTerm.nature, MyDescriptorType.AutoColorCorrelogram, MyLearnerType.svm),
	Nature2(MyTerm.nature, MyDescriptorType.PHOG, MyLearnerType.svm),
	Sky(MyTerm.sky, MyDescriptorType.JCD, MyLearnerType.svm),
	Snow(MyTerm.snow, MyDescriptorType.PHOG, MyLearnerType.svm),
	Urban1(MyTerm.urban, MyDescriptorType.AutoColorCorrelogram, MyLearnerType.svm),
	Urban2(MyTerm.urban, MyDescriptorType.PHOG, MyLearnerType.svm);



	private final MyTerm term;
	private final MyDescriptorType descriptorType;
	private final MyLearnerType learnerType;
	
	private MyModel(MyTerm t, MyDescriptorType d, MyLearnerType l) {
		this.term = t;
		this.descriptorType = d;
		this.learnerType = l;
	}

	public MyTerm getTerm() {
		return term;
	}

	public MyDescriptorType getDescriptorType() {
		return descriptorType;
	}

	public MyLearnerType getLearnerType() {
		return learnerType;
	}
	
	
	
	
}
