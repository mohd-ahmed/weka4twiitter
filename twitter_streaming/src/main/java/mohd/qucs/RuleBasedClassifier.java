package mohd.qucs;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Logger;

import weka.classifiers.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.rules.JRip;
import weka.classifiers.rules.JRip.Antd;
import weka.classifiers.rules.JRip.RipperRule;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ArffLoader;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 * This class implements a simple text learner by a classification using RIPPER  Rule based  classifier RIPPER  . It loads a
 * tow  data sets file written in ARFF format and merge them, evaluates and learn a classifier on it
 * .
 * 
 * 
 */
public class RuleBasedClassifier {
	private final static Logger logger = Logger
			.getLogger(RuleBasedClassifier.class.getName());
	/**
	 * Object that stores training data.
	 */
	Instances trainData;
	/**
	 * Object that stores the filter
	 */
	StringToWordVector filter;
	/**
	 * Object that stores the classifier
	 */
	FilteredClassifier classifier;
	String firstLabel;
	String secondLabel;

	

	public RuleBasedClassifier() {
		
	}

	public RuleBasedClassifier(String firstLabel, String secondLabel) {
	
		this.firstLabel = firstLabel;
		this.secondLabel = secondLabel;
	}

	/**
	 * This method loads a dataset in ARFF format. If the file does not exist,
	 * or it has a wrong format, the attribute trainData is null.
	 * 
	 * @param firstCalssPath The name of the file that stores the first dataset.
	 * @param secondCalssPath The name of the file that stores the second dataset.
	 * @throws IOException
	 */
	public void loadDataset(String firstCalssPath, String secondCalssPath)
			throws IOException {
		final Logger logger = Logger.getLogger(FilteredClassifier.class
				.getName());
		// make instances of first file
		ArffLoader loader = new ArffLoader();
		loader.setSource(new File(firstCalssPath));
	
		Instances instances1 = loader.getDataSet();
		// make instances of the second file

		loader.setSource(new File(secondCalssPath));
		Instances instances2 = loader.getDataSet();
		
		logger.info(instances1.numInstances() + " class 1 instances");
		logger.info(instances2.numInstances() + " class 2 instances");

		// merge the two instances
		instances2.addAll(instances1);
		trainData = instances2;
		trainData.setClassIndex(1);// index of the class if it is the last
		// attr
		// set the class labels
		firstLabel = trainData.classAttribute().value(0);
		secondLabel = trainData.classAttribute().value(1);
		logger.info(trainData.numInstances() + " of class"+ firstLabel+" and "+secondLabel+" after merge");
		
	}

	/**
	 * This method evaluates the classifier. As recommended by WEKA
	 * documentation, the classifier is defined but not trained yet. Evaluation
	 * of previously trained classifiers can lead to unexpected results.
	 */
	public void evaluate() {
		try {
			trainData.setClassIndex(1);// index of the class if it is the last
										// attr
			filter = new StringToWordVector();
			filter.setAttributeIndices("first");// index of the String
			classifier = new FilteredClassifier();
			classifier.setFilter(filter);
			JRip jrip = new JRip();
			classifier.setClassifier(jrip);
			Evaluation eval = new Evaluation(trainData);

			eval.crossValidateModel(classifier, trainData, 4, new Random(1));

			System.out.println(eval.toSummaryString());
			System.out.println(eval.toClassDetailsString());
			System.out
					.println("===== Evaluating on filtered (training) dataset done =====");

			Enumeration<Instance> enI = trainData.enumerateAttributes();

			while (enI.hasMoreElements()) {
				Attribute instance = (Attribute) enI.nextElement();
				logger.info(instance.toString());
			}
		} catch (Exception e) {
			System.out.println("Problem found when evaluating");
		}
	}

	/**
	 * This method trains the classifier on the loaded dataset.
	 * @return ArrayList<String> represents the learned rules
	 * each rule is a String of words separated by space
	 * @throws Exception 
	 */
	public ArrayList<String> learn() throws Exception {
		String[][] keywords=null;
		ArrayList<String> filters =new	ArrayList<String>();	
			/* building the classifier **/
		// index of the class if it is the last
		    trainData.setClassIndex(trainData.numAttributes()-1);
			filter = new StringToWordVector();
			filter.setAttributeIndices("first");// index of the String
			classifier = new FilteredClassifier();
			filter.setAttributeIndices("first-last");
		    filter.setPeriodicPruning(-1.0);
		    filter.setStopwords(new File("data/stopword.txt"));
		//	filter.setOptions(Utils.splitOptions( "-R first-last -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -M 1"));
			classifier.setFilter(filter);
			JRip jrip = new JRip();
			String[] options = Utils.splitOptions("-F 3 -N 2.0 -O 2 -S 1");
			jrip.setOptions( options);
			classifier.setClassifier(jrip);
			System.out.println("building classifier ");
			classifier.buildClassifier(trainData);
			
            /*extracting the rules for classalabel 1*/
			Iterator<RipperRule> it = jrip.getRuleset().iterator();
			Attribute label = trainData.classAttribute(); // class nominal
															// attribute
			System.out.println("index of the desired label " + label.value(0)); // index of the desired label
														
			while (it.hasNext()) { // iterate over each rule

				RipperRule rr = it.next();

				double consequenceIndex = rr.getConsequent(); // index value of the classified label
																
				System.out.println("con :" + consequenceIndex);
				
				/* extraction firstLabel rule      **/
				if (label.value((int) consequenceIndex).equals(firstLabel)) {
                    StringBuilder filter =new StringBuilder();
					if (rr.hasAntds()) {

						@SuppressWarnings("deprecation")
						FastVector<JRip.Antd> antds = (FastVector<JRip.Antd>) rr.getAntds();

						
						label.value((int) consequenceIndex).equals("0");
					
						for (Iterator<Antd> iterator = antds.iterator(); iterator.hasNext();) {
                              //TODO FIND SOME WAY TO CONSIDER THE ACCURECY;
							
							JRip.Antd antd = iterator.next();
							
							filter.append(antd.getAttr().name()+" ");
							

							System.out.print(antd.getAttr().name() + "=====>>>"
									+ label.value((int) consequenceIndex));
							
						}
						filters.add(filter.toString().trim());
						System.out.println();
					}
				}else{// no rule antecedent for the first Label
					
					
				}
			}
			System.out.println("classifier :::" + jrip);
			System.out
				.println("===== Training on filtered (training) dataset done =====");
			
		return filters;
	}

}