package mohd.qucs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import twitter4j.FilterQuery;
import twitter4j.Status;

public class Main {

	private static volatile int start = 0;
	private static volatile int repeat = 1;
	private static String fileForClass1 = "data\\class1";
	private static String fileForClass2 = "data\\class2";
	private static String curruntFile = fileForClass2;
	private static String fileExtention = ".arff";
	private static long time_dration = 30L;
	private static TimeUnit timeUnit = TimeUnit.SECONDS;
    private static String label_1="in";
    private static String label_2="out";
    private static String curruntLabel=label_2;
    private static String fileHeader="@relation twitter\n@attribute Text string\n@attribute class-att {"+label_1+","+label_2+"}\n\n@data\n";
    private static QueryManger queryManger=new QueryManger();
    private final static Logger logger = Logger.getLogger(Main.class.getName());
    
	public static void main(String[] args) throws SecurityException, IOException {
		
		// get parameters form configfile
			setLogin();
		
		// prepare initial query
		FilterQuery fq = init(queryManger);
		logger.info("setting fq"+fq.toString());
		
		// starting the stream
		ArrayList<Status> statusQueue = new ArrayList<Status>();
		final TwitterStreamReciever twitterStreamReciever = new TwitterStreamReciever(statusQueue);
		twitterStreamReciever.filter(fq);
	    logger.info("strating tweeter stream consumption"); 
	    
		// starting the Timer
		final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
		logger.info("starting the timer for ..."+time_dration+" seconds repeated "+repeat +" times");
		
		// prepare runnable task with delay x and period x
		scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
			public  void run() {
				start++;
				twitterStreamReciever.getTwitterStream().cleanUp();
				twitterStreamReciever.getTwitterStream().shutdown();
				String [] keywords=null;
				logger.info("close the stream and proccessing round : " + start);
				
				// processing the previous session
				String filePath= getFileName();
				try {
					logger.info("writing parsed tweets to "+filePath);
					BufferedWriter bw = getBufferedWriter(filePath);
					bw.write(fileHeader);
                 // parse and write tweets
					logger.info("que size is"+twitterStreamReciever.getStatusQueue().size());
					new TweetsParser(twitterStreamReciever.getStatusQueue(),curruntLabel).WriteToFIle(bw);
                    logger.info("done writing");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// start the weka classifier
				if (curruntLabel.equals(label_1)) {
					
					RuleBasedClassifier ruleBasedClassifier = new RuleBasedClassifier("in","out");
					
						try {
							ruleBasedClassifier.loadDataset("data/class1.arff", "data/class2.arff");
							ArrayList<String> result = 	ruleBasedClassifier.learn();
							keywords= new String[result.size()];
							logger.info("the learned keywords: "+result.toString());
							keywords= result.toArray(keywords);
							logger.info("printing result array:"+Arrays.toString(keywords));
							queryManger.addToKeyWords(keywords);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					 
					logger.info("collectin for in");
				}else{// collecting for out labe1 
				//	start=end;
					
					logger.info("collectin for out so only one round");
				}
				logger.info("Executed! : round " + start);
				
				// check to stop the session or to continue
				if (start >= repeat) {
					logger.info("shut down tasks");
					scheduledExecutorService.shutdown();
					
				}else{
					
					logger.info("restarting the strem with the new filter query "+"start"+start+"end"+repeat  );
					twitterStreamReciever.filter(queryManger.getFilterQuery());		
				}
			}
		}, time_dration, time_dration, timeUnit);

	}

	
	public static ArrayList<String> trainClassifier() throws Exception{
		
		
		RuleBasedClassifier classifier = new RuleBasedClassifier(
				label_1, label_2);
		try {
			classifier.loadDataset(fileForClass1, fileForClass2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayList<String> keywords = classifier.learn();

		queryManger.addToKeyWords(keywords.toArray(new String[keywords.size()]));
		return keywords;
	}
	

	public static void setLogin() throws SecurityException, IOException{
		FileHandler handler = new FileHandler( "log\\main.txt");
		Logger rootLogger = Logger.getLogger("");
		rootLogger.addHandler(handler);
		handler.setFormatter(new SimpleFormatter());
		logger.info("done ,,,,    ");
	}
	
	public static FilterQuery  init( QueryManger queryManager ){
		
		Properties properties = new Properties();
		InputStream input = null;

		FilterQuery fq = new FilterQuery();
		try {

			// load a properties file from class path, inside static method
			FileInputStream in = new FileInputStream("config.properties");
			properties.load(in);
			in.close();

			// prepare the initial filterQuey
			String[] keywords = null;
			if (properties.getProperty("keywords") != null
					&& !properties.getProperty("keywords").isEmpty()) {
				keywords = properties.getProperty("keywords").split(",");
				queryManager.setKeywords(keywords);
				System.out.println("key words" + keywords.toString());
				fq.track(keywords);
				System.out.println(fq.toString());
			}

			// String[] languages = { "en" };

			String[] languages = null;
			if (!properties.getProperty("languages").isEmpty()) {
				languages = properties.getProperty("languages").split(",");
				System.out.println("key words " + languages[0]);
				queryManager.setLanguages(languages);
				fq.language(languages);

			}

			double[][] box = new double[2][2];
			if (!properties.getProperty("box").isEmpty()) {
				int i = 0, j;
				for (String str : properties.getProperty("box").split(",")) {
					j = 0;
					for (String s : str.split("\\s+")) {

						box[i][j] = Double.parseDouble(s);
						// System.out.println(s);
						System.out.println(box[i][j]);
						j++;
					}
					i++;
				}
				queryManager.setBox(box);
				System.out.println("deep " + Arrays.deepToString(box));
				// keywords = prop.getProperty("box").split(",");
				fq.locations(box);
			}

			// set init param

			String[] params = { "time_dration", "repeat", "label_1",
					"label_2", "curruntLabel", "fileForClass1",
					"fileForClass2", "fileExtention" };

			for (String s : params) {

				if (properties.containsKey(s)) {

					switch (s) {
					case "time_dration":
						time_dration = Long.parseLong(properties.getProperty(s));
						break;
					case "repeat":
						repeat = Integer.parseInt(   properties.getProperty(s));
						break;

					case "label_1":
						label_1 =    properties.getProperty(s);
						break;
					case "label_2":
						label_2 =    properties.getProperty(s);
						break;
					case "curruntLabel":
						curruntLabel =    properties.getProperty(s);
						curruntFile=curruntLabel.equals(label_1)? fileForClass1:fileForClass2;
						break;
					case "fileForClass1":
						fileForClass1 =    properties.getProperty(s);
						break;
					case "fileForClass2":
						fileForClass2 =    properties.getProperty(s);
						break;
					case "fileExtention":
						fileExtention =    properties.getProperty(s);
						break;

					default:
						break;
					}
                System.out.println(s+" = "+properties.getProperty(s));
				}

			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println(fq.toString());
		return fq;

	}
	


	public static FilterQuery getFilterQuery() {

		// twitterStreamReciever.getTwitterStream().shutdown();

		// prepare the filterQuey
		String[] keywords = { "football ", "world cup" };
		String[] languages = { "en" };
		FilterQuery fq = new FilterQuery();
		fq.track(keywords);
		fq.language(languages);

		return fq;
	}

	public static BufferedWriter getBufferedWriter(String f) throws IOException {

		String fname = f;
		File file = new File(fname);
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw,1048*12);

		} catch (IOException ioe) {

		}

		return bw;

	}

	public static String getFileName() {
		return curruntFile+fileExtention;
	}

}
