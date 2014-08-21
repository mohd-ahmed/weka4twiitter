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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setLogin() throws SecurityException, IOException {
		FileHandler handler = new FileHandler("log\\parser.txt");
		Logger rootLogger = Logger.getLogger("");
		logger.addHandler(handler);
		handler.setFormatter(new SimpleFormatter());
	//	logger.info("done ,,,,    ");
	}

	public ArrayList<Status> getTweetsQueue() {
		return tweetsQueue;
	}

	public void setTweetsQueue(ArrayList<Status> tweetsQueue) {
		this.tweetsQueue = tweetsQueue;
	}

	public void WriteToFIle(BufferedWriter bw) {

		
		// logger.info("before while");
		for (Status sts : tweetsQueue) {

		//	logger.info("just  for with :" + sts.getText());

			String str = parse(sts.getText());
	//		logger.info("after calling parse");
			// String str= sts.getText();
			// System.out.println("'"+str+"'");
			try {
				bw.write("\"" + str + "\"" + "," + label + "\n");
			//	logger.info("done writing");
				//writer.write(sts.getText());
			//	writer.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		//	logger.info("after wrting parse");
		}
		try {
			tweetsQueue.clear();
			bw.close();
			//writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String parsecopy(String str) {

		StringBuilder sb = new StringBuilder();
		// String deliminator = "\\s+|:|[,]+|;|[\\.]+\\b";
		String deliminator = "\\s+";
		String[] tokens = str.split(deliminator);
		for (String token : tokens) {
			// String token = tokenizer.nextToken();
			if (token.charAt(0) == '\'' || token.charAt(0) == ':'
					|| token.charAt(0) == '"' || token.charAt(0) == '.')
				token = token.substring(0);
			if (token.charAt(token.length() - 1) == '\''
					|| token.charAt(token.length() - 1) == '"'
					|| token.charAt(token.length() - 1) == ':'
					|| token.charAt(token.length() - 1) == '.')
				token = token.substring(0, token.length() - 1);

			sb.append(token.toLowerCase() + " ");
		}

		// todo add regex
		return sb.toString().trim();
	}

	public String parse(String str) {
		//logger.info("entering parse ");
		StringBuilder sb = new StringBuilder();
		String deliminator = "\\s+|(:\\s)|,|\\+|;+|(\\.\\B)|\\?+|\\?+|!+|\"+|'+|\\(+|\\)+|:+|\\-+|\\|+|\\\\+|/+|\\=+";
		// String deliminator = "\\s+";
		String[] tokens = str.split(deliminator);
		// logger.info("tokenization ");
		for (String token : tokens) {
			//logger.info(token);

			if (token.equalsIgnoreCase("http") || token.equals("https")|| token.length() <3 || token.matches("\\?+"))
			continue;
			if (token.matches("\\W+")) {

				token.replaceAll("\\W+", " ");
			}

			if (token.matches("\\w+"))
				token.toLowerCase(Locale.ENGLISH);
		
			if (!token.isEmpty())
				sb.append(token + " ");
			
		}

		//logger.info("return :" + sb.toString().trim());

		// todo add regex
		return sb.toString().trim();
	}

}
