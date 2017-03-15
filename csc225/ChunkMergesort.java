 /* University of Victoria
 * CSC 225 - Fall 2016
 * Assignment 2 - Template for ChunkMergesort
 * 
 * This template includes some testing code to help verify the implementation.
 * To interactively provide test inputs, run the program with:
 * 
 *     java ChunkMergesort
 * 
 * To conveniently test the algorithm with a large input, create a text file
 * containing space-separated integer values and run the program with:
 * 
 *     java ChunkMergesort file.txt
 * 
 * where file.txt is replaced by the name of the text file.
 * 
 * Miguel Jimenez
 * 
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Do not change the name of the ChunkMergesort class
public final class ChunkMergesort {
	
	/**
	 * Use this class to return two lists.
	 * 
	 * Example of use:
	 * 
	 * Chunks p = new Chunks(S1, S2); // where S1 and S2 are lists of integers
	 */
	public static final class Chunks {
		
		private final List<Integer> left;
		private final List<Integer> right;
		
		public Chunks(List<Integer> left, List<Integer> right) {
			this.left = left;
			this.right = right;
		}
		
		public List<Integer> left() {
			return this.left;
		}
		
		public List<Integer> right() {
			return this.right;
		}
	}

	/**
	 * The list containing the integer comparisons in order of occurrence. Use
	 * this list to persist the comparisons; the method report will be invoked
	 * to write a text file containing this information.
	 * 
	 * Example of use (when comparing 1 and 9):
	 * 
	 * Integer[] d = new Integer[2];
	 * d[0] = 1;
	 * d[1] = 9;
	 * this.comparisons.add(d);
	 * 
	 * or just:
	 * 
	 * this.comparisons.add(new Integer[]{1, 9});
	 */
	private final List<Integer[]> comparisons;

	public ChunkMergesort() {
		this.comparisons = new ArrayList<Integer[]>();
	}

	public List<Integer> chunkMergesort(List<Integer> S) {
		int chunk = 1;
		Chunks m = null;	
		List<Integer> sorted;
		for (int i = 0; i+1 < S.size();i++){
			if (S.get(i+1)<S.get(i)){
				chunk++;
				
			}
			this.comparisons.add(new Integer[]{S.get(i), S.get(i+1)});
			
		}
		
		if (chunk==1){
			return S;
		}
		while (chunk>1){
			m = chunkDivide(S, chunk);
			List<Integer> l = chunkMergesort(m.left);
			List<Integer> r = chunkMergesort(m.right);
			sorted = merge(l,r);
			return sorted;
		}
		return null;
		
		
	}

	public Chunks chunkDivide(List<Integer> S, int c) {
		List<Integer> leftSide = new ArrayList<Integer>();
		List<Integer> rightSide = new ArrayList<Integer>();
		int mid = 0;
		int counter = 0;
		int mididx = 0;
		if (c%2 != 0){
			mid = c/2 +1;
		}else{
			mid = c/2;
		}
		
		
		for (int i = 1; i < S.size()&&counter<mid;i++){
			if (S.get(i-1)>S.get(i)){
				counter++;
				mididx = i;
			}
			
			
		}
		
		for (int i = 0; i < mididx;i++){
				leftSide.add(S.get(i));
		}
		
		
		for (int i = mididx; i < S.size();i++){
				rightSide.add(S.get(i));
		}
		
		Chunks a = new Chunks(leftSide,rightSide);
		return a;
		
		
	}

	public List<Integer> merge(List<Integer> S1, List<Integer> S2) {
		List<Integer> end = new ArrayList<Integer>();
		while (S1.size()!=0 && S2.size()!=0){
			this.comparisons.add(new Integer[]{S1.get(0), S2.get(0)});
			if (S1.get(0)<S2.get(0)){
				end.add(S1.remove(0));
			}else{
				end.add(S2.remove(0));
			}	
		}
		while (S1.size()!=0){
			end.add(S1.remove(0));
		}
		while (S2.size()!=0){
			end.add(S2.remove(0));
		}
		return end;
	}

	/**
	 * Writes a text file containing all the integer comparisons in order of
	 * ocurrence.
	 * 
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	public void report() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("comparisons.txt", false));
		for (Integer[] data : this.comparisons)
			writer.append(data[0] + " " + data[1] + "\n");
		writer.close();
	}

	/**
	 * Contains code to test the chunkMergesort method. Nothing in this method
	 * will be marked. You are free to change the provided code to test your
	 * implementation, but only the contents of the methods above will be
	 * considered during marking.
	 */
	public static void main(String[] args) {
		Scanner s;
		if (args.length > 0) {
			try {
				s = new Scanner(new File(args[0]));
			} catch (java.io.FileNotFoundException e) {
				System.out.printf("Unable to open %s\n", args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n", args[0]);
		} else {
			s = new Scanner(System.in);
			System.out.printf("Enter a list of integers:\n");
		}
		List<Integer> inputList = new ArrayList<Integer>();

		int v;
		while (s.hasNextInt() && (v = s.nextInt()) >= 0)
			inputList.add(v);

		s.close();
		System.out.printf("Read %d values.\n", inputList.size());

		long startTime = System.currentTimeMillis();

		ChunkMergesort mergesort = new ChunkMergesort();
		List<Integer> sorted = mergesort.chunkMergesort(inputList);
		for (int i: sorted){
			System.out.printf(i+", ");
		}

		long endTime = System.currentTimeMillis();
		double totalTimeSeconds = (endTime - startTime) / 1000.0;

		System.out.printf("Total Time (seconds): %.2f\n", totalTimeSeconds);
		
		try {
			mergesort.report();
			System.out.println("Report written to comparisons.txt");
		} catch (IOException e) {
			System.out.println("Unable to write file comparisons.txt");
			return;
		}
	}

}
