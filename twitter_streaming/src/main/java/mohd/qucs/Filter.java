package mohd.qucs;

import java.util.Arrays;

public class Filter implements Comparable<Filter>{
	/**
	   * the simple stats of one rule, including 6 parameters: 0: coverage;
	   * 1:uncoverage; 2: true positive; 3: true negatives; 4: false positives; 5:false negatives
	   */
	double [] stats ;
	String filterString;
	int labelIndex;
	String labelName;
	public Filter(double[] stats, String filterString, int labelIndex,
			String labelName) {
		super();
		this.stats = stats;
		this.filterString = filterString;
		this.labelIndex = labelIndex;
		this.labelName = labelName;
	}
	@Override
	public int compareTo(Filter o) {
		// TODO Auto-generated method stub
		return ( accuracy() >= o.accuracy())? 0:1 ;
	}
	
	
	public double accuracy(){
		
	return 	stats[2]/stats[0];
	}
	@Override
	public String toString() {
		return "Filter [stats=" + Arrays.toString(stats) + ", filterString="
				+ filterString + ", labelIndex=" + labelIndex + ", labelName="
				+ labelName + "]";
	}

}
