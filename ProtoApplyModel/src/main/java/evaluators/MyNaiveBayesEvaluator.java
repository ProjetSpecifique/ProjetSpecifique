package evaluators;

import org.dmg.pmml.NaiveBayesModel;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.ModelEvaluator;
import org.jpmml.evaluator.NaiveBayesModelEvaluator;


//https://github.com/jpmml/jpmml-evaluator
//https://github.com/jpmml/jpmml-example/blob/master/src/main/java/org/jpmml/example/EvaluationExample.java


public class MyNaiveBayesEvaluator {
	PMML pmml = new PMML(); //TODO

	ModelEvaluator<NaiveBayesModel> bayesEvaluator = new NaiveBayesModelEvaluator(pmml);


}
