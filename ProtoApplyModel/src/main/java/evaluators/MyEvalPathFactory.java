package evaluators;

import descripteurs.MyDescriptorType;

public class MyEvalPathFactory {

	public static String buildModelPath(MyDescriptorType descriptorType, MyLearnerType learnerType) {
		// TODO change is something changes
		String descriptorName = descriptorType.name();
		String learnerString = learnerType.name();

		return "../models/" + learnerString + "_coastline_" + descriptorName + ".pmml";
	}
}
