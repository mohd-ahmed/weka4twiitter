package mohd.qucs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;



import java.util.logging.Logger;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;


/**
 * this class manage connection to the twitter stream and collects the tweets based on the
 * given Filter Query and save the collected tweets to a queue
 * @author mohd
 *
 *
 */

public class TwitterStreamReciever {
	
	private ArrayList<Status>  statusQueue ;
	

	private final TwitterStream twitterStream ;
	
	private final static Logger log = Logger.getLogger(TwitterStreamReciever.class.getName());
	public TwitterStreamReciever(ArrayList<Status>  statusQueue) {
		
		this.statusQueue=statusQueue;
		twitterStream= new TwitterStreamFactory().getInstance();
		
        twitterStream.addListener(new TwitterListener());

	}
	
	public ArrayList<Status>  getStatusQueue() {
		return statusQueue;
	}

	public void setStatusQueue(ArrayList<Status>  statusQueue) {
		this.statusQueue = statusQueue;
	}

	
	public void filter (FilterQuery fq){
		 twitterStream.filter(fq);
		
	}
	
	public TwitterStream getTwitterStream() {
		return twitterStream;
	}

	private class TwitterListener implements StatusListener {
       
        public void onStatus(final Status status) {
        	
        	statusQueue.add(status);
        }

        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
            log.info("Received a status deletion notice id:" + statusDeletionNotice.getStatusId());
        }

        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
            log.info("Received track limitation notice:" + numberOfLimitedStatuses);
        }

     
        public void onScrubGeo(long userId, long upToStatusId) {
            log.info("Received scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
        }

        
        public void onStallWarning(StallWarning warning) {
            log.info("Received stall warning:" + warning);
        }
 
        public void onException(Exception ex) {
            log.info("Received Exception: "+ex.getMessage());
        }
    }

}
