/*
 * This is partial code for building the root of a decision tree using the SLIQ algorithm. 
 * Only the numeric attributes are to be considered in this exercise.
 * Search for "Write your code" to see where you need to add code.  
 */

import java.io.*;
import java.util.*;

public class SLIQrootPartial {

	String[] attributes;
	Boolean[] isNumeric;
	Set<String> ClassValues = new HashSet<String>();
	Integer yIndex; //this is index of the class attribute
	
	class SplitTest {
		String attribute;
		Boolean numeric; //The program only handles numeric attributes for now.
		Object splitval; //Numeric: A<=v. Categorical: A==v
		Double entropy;
		
		public String toString() {
			if (numeric) return attribute + "<=" + splitval;
			else return attribute + "==" + splitval;
		}
	}
	
	//rid-classlabel structure. 
	//The structure is a Map. Given an rid, we can quickly find the class value for the record.
	Map<Integer, String> ClassList = new HashMap<Integer, String>(); 
	
	//A histogram is nothing else, but a list of classvalue-count pairs. 
	//For example, ["yes":5, "no":7] is a histogram, saying that 
	//there are 5 tuples with "yes" as class value, and 7 tuples with "no" as class value.
	//We organize a histogram as a map indexed by class values. 
	//Given a class value, we can quickly find the corresponding count.
	//Here we are creating the root histogram that is the overall histogram of
	//class values and their counts for the dataset as a whole.
	Map<String, Integer> roothistogram = new HashMap<String, Integer>(); 
	
	//We construct a histogram pair for each numeric attribute.
	//Why a pair of histograms? 
	//Well, given a threshold for the attribute, the tuples will be split
	//into a left bag and a right bag. 
	//Each resulting bag needs a histogram of classvalue-count pairs.
	//These histograms will be use to compute the entropy of the split. 
	class HistPair {
		Map<String, Integer> histogram0;
		Map<String, Integer> histogram1;		
	}
	
	//Histogram pairs are stored in a map indexed by the attribute names.
	//For each attribute, we want to record the histogram of the best split, in terms of entropy.
	Map<String, HistPair> att_histpair_map = new HashMap<String, HistPair>();
	
	//This is the separator for the text file we read, i.e. tab or space
	String sep = "\t"; 
	
	
	public SLIQrootPartial(String filename, int yIndex) throws Exception {
		
	    this.yIndex = yIndex;
		
		BufferedReader datafile = new BufferedReader( new FileReader(filename) );
		
		//read first line of data containing the header, i.e. attribute names, 
		//and allocate the "attributes" and "isNumeric" arrays.
        attributes = datafile.readLine().split(sep);
        isNumeric = new Boolean[attributes.length];
        
        //create a file for each (non-class) attribute
        //we will use an array of BufferedWriter's
        //the length of the array will be the number of attributes, i.e. attributes.length
        BufferedWriter[] attributefiles = new BufferedWriter[attributes.length];
        for(int i=0; i<attributes.length; i++)
        	if(i != yIndex) 
        		attributefiles[i] = new BufferedWriter( new FileWriter(attributes[i]+".txt") );
        
        //read second line of data and determine numeric or not for attributes
        String[] strArray = datafile.readLine().split(sep);
        for(int i=0; i<attributes.length; i++) {
        	if(i == yIndex) {
        		isNumeric[i] = false;
        		continue;
        	}
        	
        	try { 
        		Double.valueOf(strArray[i]);
        		isNumeric[i] = true;
        	}
        	catch(NumberFormatException nfe) { 
        		isNumeric[i] = false;
        	}
        }
        
        //Write a separate text file for each attribute.
        //Introduce rid for each record. We assume rid does exist initially for a dataset.
        //If the attribute is the class one, add to ClassList and update roothistogram.
        Integer rid=0;
        do {
        	for(int i=0; i<attributes.length; i++) {
        		if(i != yIndex) {
        			attributefiles[i].write(rid + sep + strArray[i] + "\n");
        		}
        		else {
        			String classval = strArray[i];
        			ClassValues.add(classval);
        			ClassList.put(rid, classval);
        			
        			int cnt = 0;
        			if (roothistogram.containsKey(classval)) 
        				cnt = roothistogram.get(classval);
        			roothistogram.put(classval, cnt+1);
        		}
        	}
        	rid++;
        	
        	String line = datafile.readLine();
        	if(line == null) break;
        	strArray = line.split(sep);
        	
        } while ( true );
               
        //closing files
        for(int i=0; i<attributes.length; i++)
        	if(i != yIndex) attributefiles[i].close();
        datafile.close();
        
	    //sort the attribute files; in windows install first gnu coreutils
        //sort performs a disk-based two-phase multiway merge sort.  
        //adjust the path for your installation.
		//In Mac or Linux, sort is already installed; just change 
		//sortpath below to:
		//String sortpath = "sort";
		System.out.println("Sorting attribute files... ");
		
        String sortpath = "\"C:/Program Files (x86)/GnuWin32/bin/sort.exe\"";
        for(int i=0; i<attributes.length; i++)
        	if(i != yIndex && isNumeric[i]) {
        		String sortcmd = sortpath + " -nk 2 " + attributes[i] + ".txt -o " + attributes[i] + ".txt";
        		System.out.println(sortcmd);
        		Runtime.getRuntime().exec(sortcmd).waitFor();
        	}
        
        
        System.out.println("Finished sorting attribute files. ");
	}
		
			
	//i specifies the index of an attribute.
	//The return value is the best SplitTest for the specified attribute.
	
	SplitTest EvaluateSplits(int i) throws Exception {
		if(i==this.yIndex || isNumeric[i]==false) return null;
		
		String A = this.attributes[i]; 
		//System.out.println("Processing file "+A+".txt");
		BufferedReader Afile = new BufferedReader( new FileReader(A+".txt") );
		String line;
		SplitTest stest = null;
		double entropy_split_min = Double.MAX_VALUE;
		
		//Initialize the histogram pair for attribute A. 
		att_histpair_map.put(A, createHistPair(roothistogram));
		
		Double prev = Double.MIN_VALUE;
		

		/* Complete this while loop */
		/* Hint: Make use of those auxiliary methods, defined below */
		while( (line=Afile.readLine()) != null ) {	

			String[] strArray = line.split(sep);
			Integer rid = Integer.parseInt(strArray[0]);
			Double v = Double.parseDouble(strArray[1]);
			
			//If v is a new value
			if(prev < v) {
				double entropy_split = entropySplit(
							att_histpair_map.get(A).histogram0, 
							att_histpair_map.get(A).histogram1);
				
				//Print debug information
				//System.out.println("Entropy for "+prev + " is " + entropy_split);

				//Record a better split based on the value of entropy_split
				if ( entropy_split < entropy_split_min ) {
					stest = new SplitTest();
					stest.attribute = A; stest.numeric = true; stest.splitval = prev;
					stest.entropy = entropy_split;
					entropy_split_min = entropy_split; 
					;
				}
				
				prev=v;
			}
			
			String classval = ClassList.get(rid);
			
			updateHistograms(att_histpair_map.get(A).histogram0, 
							 att_histpair_map.get(A).histogram1, 
							 classval, 1);
			
		}
		Afile.close();
		
		return stest;
	}
	
	//Output the best attribute split
	SplitTest BestAttributeSplitTest() throws Exception {
		Double minentropy = Double.MAX_VALUE;
		SplitTest minstest = null;
		
		for(int i=0; i<this.attributes.length; i++) {
			SplitTest stest = this.EvaluateSplits(i);
			if (stest == null) 
				continue;
			if(stest.entropy < minentropy) {
				minentropy = stest.entropy;
				minstest = stest;
			}
		}
		
		return minstest;
	}
	
	//update the histogram of each class
	void updateHistograms(Map<String, Integer> h0, Map<String, Integer> h1, String classval, Integer delta) {
		h0.put(classval, h0.get(classval)+delta);
		h1.put(classval, h1.get(classval)-delta);
	}

	/*
	 * ************************************************************
	 * Auxiliary methods
	 * ************************************************************
	 */
	
	Map<String, Integer> zeroHist() {
		Map<String, Integer> result = new HashMap<String, Integer>();
		for(String classval : ClassValues) 
			result.put(classval, 0);
		return result;
	}
	
	HistPair createHistPair (Map<String,Integer> histogram) {
		HistPair hp = new HistPair();
		hp.histogram1 = new HashMap<String,Integer>(histogram);
		hp.histogram0 = zeroHist();
		return hp;
	}
	
	//Compute the entropy of a histogram
	Double entropyHistogram(Map<String, Integer> histogram) {
		Double result = 0.0;
		Integer sum = 0;
		
		for(String classval : histogram.keySet()) 
			sum += histogram.get(classval);
		
		for(String classval : histogram.keySet()) {
			Double r = histogram.get(classval)/(double)sum;
			if(r>0) result -= r*Math.log(r)/Math.log(2);
		}
		
		return result;
	}
	
	//Compute the weighted average entropy of the split (See Slide 17 in decision_trees.pptx)
	//Hint: This function will be using the above entropyHistogram()
	Double entropySplit(List< Map<String, Integer> > histograms) {
		Double result = 0.0;
		Integer sumall = 0;

		List<Integer> sumList = new ArrayList<Integer>();
		List<Double> entropyList = new ArrayList<Double>();
		
		for(Map<String, Integer> h : histograms) {
			Integer sum = 0;
			for(String classval : h.keySet()) 
				sum += h.get(classval);
			sumall += sum;
			sumList.add(sum);
			entropyList.add(entropyHistogram(h));
		}
		
		for(int i=0; i<sumList.size(); i++) 
			result += entropyList.get(i) * sumList.get(i)/(double)sumall;
		
		return result;
	}
	
	//Merge left and right histograms
	//The return value is the entropy of the split, calculated by the above entropySplit()
	Double entropySplit(Map<String, Integer> histogram0, Map<String, Integer> histogram1) {
		List< Map<String, Integer> > hlist = new ArrayList< Map<String, Integer> >();
		hlist.add(histogram0); hlist.add(histogram1);
		return entropySplit(hlist);
	}
	

	public static void main(String[] args) throws Exception {
		//Specify filename, class attribute index, and minNumInstancesToSplit
		SLIQrootPartial sliq = new SLIQrootPartial("car.txt", 3);
		System.out.println("Class values are: " + sliq.ClassValues);
		System.out.println(sliq.BestAttributeSplitTest());
	}
}
