package evaluators;

import main.MyTerm;
import descripteurs.MyDescriptorType;

public class MyEvalPathFactory {

	public static String buildModelPath(MyDescriptorType descriptorType, MyLearnerType learnerType, MyTerm term) {
		// TODO change is something changes
		String descriptorName = descriptorType.name();
		String learnerString = learnerType.name();
		String termString = term.name();

		return "../models/" + learnerString + "_" + termString + "_" + descriptorName + ".pmml";
	}
}
