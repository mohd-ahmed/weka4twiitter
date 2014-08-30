package mohd.qucs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import twitter4j.FilterQuery;
/**
 * this class control the filter parameters sent  to twitter stream 
 * @author mohd
 *
 */
public class QueryManger {
	
	private String[] keywords;
	private double box [][];
	private String[] languages;
	private final static Logger logger = Logger.getLogger(QueryManger.class.getName());
	
	ArrayList<Filter> list;
	//TODO STORE FILTER ACCURECIES
	
	public QueryManger(double[][] box, String[] languages)  {
		
		this.box = box;
		this.languages = languages;
		list = new ArrayList<>();
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

	public QueryManger()  {
		list = new ArrayList<>();
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
	}

	public String[] getKeywords() {
		return keywords;
	}

	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}

	public void addToKeyWords(String[] newKeys) {
		
		
		if (newKeys == null)
			throw new IllegalArgumentException();
		int len=this.keywords == null ? 0
				: this.keywords.length;
		String[] list = new String[len + newKeys.length];
		if (keywords != null) {
			for (int i = 0; i < keywords.length; i++) {
				list[i] = keywords[i];

			}
		}
		for (int i = 0; i < newKeys.length; i++) {
			list[len+ i] = newKeys[i];
		}
		keywords = list;

	}
	public double[][] getBox() {
		return box;
	}

	public void setBox(double[][] box) {
		this.box = box;
	}

	public String[] getLanguages() {
		return languages;
	}

	public void setLanguages(String[] languages) {
		this.languages = languages;
	}
	
	/**
	 * 
	 * @return FilterQuery object that wraps the next query parmeters send to twitter 
	 */
	
	 public FilterQuery getFilterQuery(){
		//TODO add some logic to select keywords
		FilterQuery fq =new FilterQuery(); 
		if( list.size() > 0 )
			fq.track(listToArray());
		else if(keywords != null)
			fq.track(keywords);
		
		if(languages!=null)
		 fq.language(languages);
		if(box!=null)
		fq.locations(box);
		logger.info("the new filter query"+fq.toString());
		return fq; 
	 }
	 
	 
	 
	 public void addFilters(ArrayList<Filter> nextList ){ 
		list.addAll(nextList);
		Collections.sort(list);
	 }
	 public String []  listToArray(){ 
		 String [] strings = new String [list.size()];
		 int index = 0;
		 for(Filter f : list){
			strings[index++]= f.filterString;		 
		 }
		return strings; 
	 }
	 
}
