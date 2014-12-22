package evaluators;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dmg.pmml.FieldName;
import org.dmg.pmml.NaiveBayesModel;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.FieldValue;
import org.jpmml.evaluator.ModelEvaluator;
import org.jpmml.evaluator.NaiveBayesModelEvaluator;
import org.jpmml.model.ImportFilter;
import org.jpmml.model.JAXBUtil;
import org.xml.sax.InputSource;

//https://github.com/jpmml/jpmml-evaluator
//https://github.com/jpmml/jpmml-example/blob/master/src/main/java/org/jpmml/example/EvaluationExample.java

public class MyNaiveBayesEvaluator {

	private static PMML pmml;
	private static ModelEvaluator<NaiveBayesModel> bayesEvaluator;
	private static String modelPath = "../models/bayes_coastline_AutoColorCorrelogram.pmml";

	static {
		// init the model
		System.out.println("Init PMML");

		try {
			pmml = JAXBUtil.unmarshalPMML(ImportFilter.apply(new InputSource(MyNaiveBayesEvaluator.class
					.getResourceAsStream(modelPath))));
			bayesEvaluator = new NaiveBayesModelEvaluator(pmml);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * // active fields = model fields List<FieldName> activeFields =
	 * bayesEvaluator.getActiveFields(); // targetFields = the class
	 * List<FieldName> targetFields = bayesEvaluator.getTargetFields(); //
	 * output fields List<FieldName> outputFields =
	 * bayesEvaluator.getOutputFields();
	 * 
	 * System.out.println(activeFields); System.out.println(targetFields);
	 * System.out.println(outputFields);
	 */

	public static void evaluate(double[] histogram) throws Exception {

		List<FieldName> activeFields = bayesEvaluator.getActiveFields();
		if (histogram.length != activeFields.size()) {
			System.out.println("Histogram length : " + histogram.length);
			System.out.println("Model length : " + activeFields.size());
			throw new Exception("diffrent lengths : histogram and model");
		}

		/* set arguments */
		Map<FieldName, FieldValue> arguments = new LinkedHashMap<FieldName, FieldValue>();
		int index;

		for (FieldName activeField : activeFields) {

			index = Integer.parseInt(activeField.toString().substring(3)) - 1;
			// index = activeFields.indexOf(index) - no correlation
			//System.out.println(activeField + " " + index);
			arguments.put(activeField, bayesEvaluator.prepare(activeField, histogram[index]));

		}

		/* evaluate */
		Map<FieldName, ?> result = bayesEvaluator.evaluate(arguments);

		/* write results */
		System.out.println("Writing " + result.size() + " result(s):");
	}
}
