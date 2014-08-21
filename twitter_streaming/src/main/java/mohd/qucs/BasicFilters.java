package mohd.qucs;

import twitter4j.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p>This is a code example of Twitter4J Streaming API - filter method support.<br>
 * Usage: java twitter4j.examples.stream.PrintFilterStream [follow(comma separated numerical user ids)] [track(comma separated filter terms)]<br>
 * </p>
 *
 */
public final class BasicFilters {

    public static void main(String[] args) throws TwitterException, IOException {
    	
    	/*
    	 * 
    	 * Charset characterSet = Charset.defaultCharset();
    int numLines = 10;
    Path path = Paths.get("OutputFile2.txt");
    try (BufferedWriter writer = 
           Files.newBufferedWriter(path, characterSet)) {
      for(int i=0; i<numLines; i++) {
        writer.write("Number is " + 100 * Math.random());
        writer.newLine();
      }
    } catch (IOException ioe) {
      System.err.printf("IOException: %s%n", foe);
    }
    	 * 
    	 * 
    	 */
    	
    	String filename ="test.txt";
    	File file = new File(filename);
    	FileWriter fw=null;
    	BufferedWriter bw=null;
    	try {
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			
    	}catch(IOException ioe){
				
			}
 
			 fw = new FileWriter(file.getAbsoluteFile());
			 bw = new BufferedWriter(fw);
			 bw.write("================================ \n");
            bw.flush();
    	
    	/*
    	 Charset characterSet = Charset.defaultCharset();
    	 Path path = Paths.get("OutputFile2.txt");*/
    	 StatusListener listener;
    	
    	
    		
    		listener = new StatusHandler(bw);
    	 
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(listener);
        
        //String[] trackArray = {"egypt world cup";
        double lat = 53.186288;
        double longitude = -8.043709;
        double lat1 = 24.491835;
        double longitude1 = 50.789795;
        double lat2 = 26.187329;
        double longitude2 = 51.564331;

        double[][] bb = {{longitude1, lat1}, {longitude2, lat2}};

        FilterQuery fq = new FilterQuery();
        String[] languages={"en"};
        fq.locations(bb);
        fq.language(languages);
        String []keyWords={"Qatar Worldcup", "Qatar University"};
        
       // fq.track(keyWords);
        twitterStream.filter(fq); 
     
        
      //FilterQuery  filter=new FilterQuery();

        // filter() method internally creates a thread which manipulates TwitterStream and calls these adequate listener methods continuously.
       // twitterStream.filter(filter);
      

    }
  
}//eofclass


class StatusHandler implements StatusListener{
	
	BufferedWriter  writer;
	
	public StatusHandler(   BufferedWriter    bw          ){
		
		this.writer=bw;	
		
	}
	
	public void onStatus(Status status) {
		
		try {
			writer.write("=================================================================== \n");
			User user = status.getUser();
			String str= "@" + status.getUser().getScreenName() + " - " + status.getText();
			String profileLocation = user.getLocation();
			long tweetId = status.getId();
			GeoLocation geolocation = status.getGeoLocation();
			
			System.out.println(str);
			writer.write(str);
			writer.write("meta data | \n");
			writer.write("profileLocation | "+profileLocation+"\n");
			writer.write("geolocation| "+geolocation+"\n");
			writer.write( " *************************************************************");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
    }

  //  @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        try {
			System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
			writer.write("Got track limitation notice:" + numberOfLimitedStatuses);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void onScrubGeo(long userId, long upToStatusId) {
        System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
        try {
			writer.write("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void onStallWarning(StallWarning warning) {
        try {
			System.out.println("Got stall warning:" + warning);
			writer.write("Got stall warning:" + warning);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void onException(Exception ex) {
        ex.printStackTrace();
       try {
		writer.flush();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
	
}














