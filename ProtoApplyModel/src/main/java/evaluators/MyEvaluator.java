package evaluators;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dmg.pmml.FieldName;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.EvaluatorUtil;
import org.jpmml.evaluator.ModelEvaluatorFactory;
import org.jpmml.evaluator.NeuronClassificationMap;
import org.jpmml.evaluator.NodeClassificationMap;
import org.jpmml.evaluator.VoteClassificationMap;
import org.jpmml.manager.PMMLManager;
import org.jpmml.model.ImportFilter;
import org.jpmml.model.JAXBUtil;
import org.xml.sax.InputSource;

public class MyEvaluator {

	public static boolean logResults = true;

	public static String evaluate(double[] histogram, String modelPath, String resultClass) throws Exception {

		Object targetValue = evaluateAndGetTargetValue(histogram, modelPath, resultClass);
		// FieldName targetName = evaluator.getTargetField();
		Double proba = getProbabilityForClass(targetValue, resultClass);
		if (logResults) {
			System.out.println("Probability for class " + resultClass + " : " + proba);
		}

		return (String) EvaluatorUtil.decode(targetValue);
	}

	public static Double evaluateProbability(double[] histogram, String modelPath, String resultClass) throws Exception {
		Object targetValue = evaluateAndGetTargetValue(histogram, modelPath, resultClass);
		// FieldName targetName = evaluator.getTargetField();
		Double proba = getProbabilityForClass(targetValue, resultClass);
		if (logResults) {
			System.out.println("Probability for class " + resultClass + " : " + proba);
		}

		return proba;
	}

	private static Object evaluateAndGetTargetValue(double[] histogram, String modelPath, String resultClass)
			throws Exception {
		PMML pmml;
		try {
			pmml = JAXBUtil.unmarshalPMML(ImportFilter.apply(new InputSource(MyNaiveBayesEvaluator.class
					.getResourceAsStream(modelPath))));
		} catch (Exception e) {
			throw new Exception("Cannot find model at : " + modelPath);
		}
		PMMLManager pmmlManager = new PMMLManager(pmml);

		// Load the default model
		Evaluator evaluator = (Evaluator) pmmlManager.getModelManager(ModelEvaluatorFactory.getInstance());

		// set arguments
		Map<FieldName, Object> arguments = new LinkedHashMap<FieldName, Object>();
		int index;
		List<FieldName> activeFields = evaluator.getActiveFields();

		for (FieldName activeField : activeFields) {
			/*
			 * !!! for rProp, svm and decision tree index ==
			 * activeFields.indexOf(activeField) NOT for bayes
			 */
			index = Integer.parseInt(activeField.toString().substring(3)) - 1;
			// System.out.println(activeField + " " + index + " " +
			// activeFields.indexOf(activeField));
			arguments.put(activeField, evaluator.prepare(activeField, histogram[index]));
		}

		// evaluate
		Map<FieldName, ?> result = evaluator.evaluate(arguments);

		if (logResults) {
			// writeResult
			System.out.println("Writing " + result.size() + " result(s):");

			List<FieldName> predictedFields = evaluator.getTargetFields();// .getPredictedFields();
			for (FieldName predictedField : predictedFields) {
				Object predictedValue = result.get(predictedField);

				System.out.println(predictedField + ": " + predictedValue);
				// System.out.println(predictedValue.getProbability("0"));
				// System.out.println(predictedValue.getProbability("1"));

				System.out.println("Winner class: " + EvaluatorUtil.decode(predictedValue));
			}
		}

		FieldName targetName = evaluator.getTargetField();
		return result.get(targetName);
	}

	private static Double getProbabilityForClass(Object targetValue, String resultClass) {

		if (targetValue instanceof NeuronClassificationMap) {
			NeuronClassificationMap result = (NeuronClassificationMap) targetValue;
			return result.getProbability(resultClass);
		} else if (targetValue instanceof NodeClassificationMap) {
			NodeClassificationMap result = (NodeClassificationMap) targetValue;
			return result.getProbability(resultClass);
		} else if (targetValue instanceof VoteClassificationMap) {
			VoteClassificationMap<?> result = (VoteClassificationMap<?>) targetValue;
			result.getProbability(resultClass);
		}

		return 0.0;
	}
}