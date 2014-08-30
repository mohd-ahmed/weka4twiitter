package mytest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import mohd.qucs.Filter;
import mohd.qucs.RuleBasedClassifier;

import org.junit.Test;

public class RuleBasedClassifierTest {

	@Test
	public void test() throws Exception  {
		RuleBasedClassifier ruleBasedClassifier = new RuleBasedClassifier("in","out");
		try {
			ruleBasedClassifier.loadDataset("data/class1.arff", "data/class2.arff");
			//ruleBasedClassifier.loadDataset("data/ReutersGrain-test.arff", "data/ReutersCorn-test.arff");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
	ArrayList<Filter> result = 	ruleBasedClassifier.learn();
	System.out.println(result);
	}
}
