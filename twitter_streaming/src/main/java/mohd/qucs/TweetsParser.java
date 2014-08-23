package mohd.qucs;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Queue;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import twitter4j.Status;

/**
 * this class parses and writes the previously collected tweets to  file
 * @author mohd
 *
 */


public class TweetsParser {

	BufferedWriter writer;
	ArrayList<Status> tweetsQueue;
	String fileHeader;
	String label;
	private final static Logger logger = Logger.getLogger(TweetsParser.class
			.getName());

	public TweetsParser() {
		try {
			setLogin();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public TweetsParser(ArrayList<Status> queue, String label) {
		this.tweetsQueue = queue;
		this.label = label;
		try {
			setLogin();
		} catch (SecurityException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
	
			e.printStackTrace();
		}
	}

	public void setLogin() throws SecurityException, IOException {
		FileHandler handler = new FileHandler("log\\parser.txt");
		Logger rootLogger = Logger.getLogger("");
		logger.addHandler(handler);
		handler.setFormatter(new SimpleFormatter());

	}

	public ArrayList<Status> getTweetsQueue() {
		return tweetsQueue;
	}

	public void setTweetsQueue(ArrayList<Status> tweetsQueue) {
		this.tweetsQueue = tweetsQueue;
	}
/**
 * writes the parsed tweets to a file wraped in a BufferedWriter Object
 * @param bw bufferedWrter object 
 */
	public void WriteToFIle(BufferedWriter bw) {

		for (Status sts : tweetsQueue) {
			String str = parse(sts.getText());
	
			try {
				bw.write("\"" + str + "\"" + "," + label + "\n");
		
			} catch (IOException e) {
				e.printStackTrace();
			}
	
		}
		try {
			tweetsQueue.clear();
			bw.close();
		} catch (IOException e) {
		
			e.printStackTrace();
		}
	}

	/**
	 * using regular expressions to tokens and remove noisy characters
	 * @param str of the original string of tweet text
	 * @return
	 */
	
	public String parse(String str) {
	
		StringBuilder sb = new StringBuilder();
		String deliminator = "\\?+|(:\\s)+|\\s+|,+|\\++|;+|(\\.\\B)|\\?+|!+|\"+|'+|\\(+|\\)+|:+|\\-+|\\|+|\\\\+|/+|\\=+";

		String[] tokens = str.split(deliminator);
	
		for (String token : tokens) {
			//logger.info(token);

			if (token.equalsIgnoreCase("http") || token.equals("https")||token.equalsIgnoreCase("rt") ||token.length() <2 || token.matches("\\?+"))
			continue;
			if (token.matches("\\W+")) {

				token.replaceAll("\\W+", " ");
			}

			if (token.matches("\\w+"))
				token.toLowerCase(Locale.ENGLISH);
		
			if (!token.isEmpty())
				sb.append(token + " ");
			
		}
		return sb.toString().trim();
	}

}
