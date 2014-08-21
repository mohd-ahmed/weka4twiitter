package mytest;


import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Queue;
import java.util.StringTokenizer;

import twitter4j.Status;

public class TweetsParser {
	
	
	Queue<Status> tweetsQueue;
	String fileHeader; 
	String label;

	public TweetsParser() {
		
		// TODO Auto-generated constructor stub
	}


	public TweetsParser(Queue<Status> queue,String label) {
		this.tweetsQueue=queue;
		this.label=label;
	}

	
	public Queue<Status> getTweetsQueue() {
		return tweetsQueue;
	}

	public void setTweetsQueue(Queue<Status> tweetsQueue) {
		this.tweetsQueue = tweetsQueue;
	}
	
	
	
	public void WriteToFIle(BufferedWriter bw) throws IOException{
	
		
		Iterator< Status>it= tweetsQueue.iterator();
		
	while(it.hasNext()){
		Status sts = it.next();
		
		String str =parse(sts.getText());
		System.out.println("'"+sts.getText()+"'");
	  
		bw.write("\""+sts.getText()+"\""+","+label+"\n");
		bw.flush();
	it.remove();
	}
		
		bw.flush();
		System.out.println("done writing....");
	}
	
	public String parsecopy (String str){
		 
		 StringBuilder sb =new StringBuilder();
		// String deliminator = "\\s+|:|[,]+|;|[\\.]+\\b";
		 String deliminator = "\\s+";
	     String [] tokens = str.split(deliminator);
	     for (String token :tokens) {
	  /*      // String token = tokenizer.nextToken();
	     if(token.charAt(0) == '\'' || token.charAt(0) == ':' || token.charAt(0) == '"' || token.charAt(0) == '.' )
	    	   token=token.substring(0);
	       if(token.charAt(token.length()-1) == '\'' ||token.charAt(token.length()-1) == '"'||token.charAt(token.length()-1) == ':' || token.charAt(token.length()-1) == '.' )
	    	   token=token.substring(0,token.length()-1); 
	       */
	    
	         sb.append(token.toLowerCase()+" ");
	     }
		 
		 // todo add regex 
		 return sb.toString().trim();
	 }
	
	public String parse (String str){
		 
		 StringBuilder sb =new StringBuilder();
		 String deliminator = "\\s+|(:\\s)|,|\\+|;+|(\\.\\B)|\\?+|\\?+|!+";
		// String deliminator = "\\s+";
	     String [] tokens = str.split(deliminator);
	     for (String token :tokens) {
	  /*      // String token = tokenizer.nextToken();
	     if(token.charAt(0) == '\'' || token.charAt(0) == ':' || token.charAt(0) == '"' || token.charAt(0) == '.' )
	    	   token=token.substring(0);
	       if(token.charAt(token.length()-1) == '\'' ||token.charAt(token.length()-1) == '"'||token.charAt(token.length()-1) == ':' || token.charAt(token.length()-1) == '.' )
	    	   token=token.substring(0,token.length()-1); 
	       */
	  //  token.replaceAll("'|\"", "`");
	    	 String [] rr;
	    if (token.contains("'")){
	    rr =token.split("'");
	    token=rr[0];
	    }
	    if (token.contains("\"")){
		    rr =token.split("\"");
		    token=rr[0];
		    }
	         sb.append(token.toLowerCase()+" ");
	     }
		 
		 // todo add regex 
		 return sb.toString().trim();
	 }
	

}
