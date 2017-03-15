/*Assignment 3 heapSort
 *
 *Paul Cheng */
import java.util.*;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;



public class PQ225
{
   

   private int size;          
   private int[] heap;     
   private int cp;
   public PQ225()
   {
      size = 0;
	  cp = 0;
      this.results = new ArrayList<String>();
   }
   
 /**
  * Construct the binary heap given an array of items.
  */
 
   private final List<String> results;
   public PQ225(int[] array)
   {
      size = array.length;
	  cp = 0;
	  this.results = new ArrayList<String>();
      heap = new int[array.length+1];

      System.arraycopy(array, 0, heap, 1, array.length);//we do not use 0 index

      makeHeap();
      
   }
	public int[] ranGen(int N,int LOW,int HIGH){
		int k = 0;
		int m[] =  new int[N];
		while (k<N){
			int rn = (int)(Math.random()*HIGH);
			if (rn > LOW && rn < HIGH){
				m[k]=rn;
				k++;
			} 
		}
		return m;
	}
	public int size(){
		return size;
	}
   /**
  *   runs at O(size)
  */
   private void makeHeap()
   {
      for (int k = size/2; k > 0; k--)
      {
         shiftDown(k);
      }
   }
   private void shiftDown(int k)
   {
      int tmp = heap[k];
      int child;

      for(; 2*k <= size; k = child)
      {
         child = 2*k;

         if(child != size && heap[child]>heap[child + 1]){ 
			 
			 cp++;
			 child++;
			 
		 }
         if(tmp>heap[child]){  
			 heap[k] = heap[child];
		    
			 cp++;
		 }
		 else{
             break;
		 }
      }
      heap[k] = tmp;
   }

 /**
  *  Sorts a given array of items.
  */
   public int heapSort(int[] array)
   {
      size = array.length;
      heap = new int[size+1];
      System.arraycopy(array, 0, heap, 1, size);
      makeHeap();

      for (int i = size; i > 0; i--)
      {
         int tmp = heap[i]; //move top item to the end of the heap array
         heap[i] = heap[1];
         heap[1] = tmp;
         size--;
         shiftDown(1);
      }
      for(int k = 0; k < heap.length-1; k++)
         array[k] = heap[heap.length - 1 - k];
	  return 1;
   }

 /**
  * Deletes the top item
  */
   public int deleteMin() 
   {
      if (size == 0){
		  return  -1;
	  }
      int min = heap[1];
      heap[1] = heap[size--];
      shiftDown(1);
      return min;
	}

 /**
  * Inserts a new item
  */
   public void insert(int x)
   {
      if(size == heap.length - 1) doubleSize();

      //Insert a new item to the end of the array
      int pos = ++size;

      //Percolate up
      for(; pos > 1 && x<heap[pos/2]; pos = pos/2 )
         heap[pos] = heap[pos/2];

      heap[pos] = x;
   }
    public boolean isSorted(int[] a) {
        for (int i = 1; i < a.length; i++)
            if ((a[i] < a[i-1])) 
				return false;
        return true;
    }
	private void doubleSize()
   {
      int [] old = heap;
      heap = new int[heap.length * 2];
      System.arraycopy(old, 1, heap, 1, size);
   }
   
    public void test()
	{
	  	
		long startTime = System.currentTimeMillis();
		int[] m = ranGen(100,0,10000);
		heapSort(m);
		results.add("Checking sorted for 100 inputs: ");
		
		if (isSorted(m)==true){
			results.add("Sorted");
			
		}else{
			results.add("Unsorted");
			
		}
		long endTime = System.currentTimeMillis();
		double totalTimeSeconds = (endTime - startTime) / 1000.0;
		results.add("Total Time (seconds): ");
		
		results.add(String.valueOf(totalTimeSeconds));
		
		results.add("Comparsion times:");
		
		results.add(String.valueOf(cp));
		
		cp = 0;
		
		long startTime1 = System.currentTimeMillis();
		int[] n = ranGen(1000,0,10000);
		heapSort(n);
		results.add("Checking sorted for 1000 inputs: ");
		
		if (isSorted(m)==true){
			results.add("Sorted");
			
		}else{
			results.add("Unsorted");
			
		}
		long endTime1 = System.currentTimeMillis();
		totalTimeSeconds = (endTime1 - startTime1) / 1000.0;
		results.add("Total Time (seconds): ");
		
		results.add(String.valueOf(totalTimeSeconds));
		
		results.add("Comparsion times:");
		
		results.add(String.valueOf(cp));
		
		cp = 0;
		
		long startTime2 = System.currentTimeMillis();
		int[] mn = ranGen(10000,0,10000);
		heapSort(mn);
		results.add("Checking sorted for 10000 inputs: ");
		
		if (isSorted(m)==true){
			results.add("Sorted");
			
		}else{
			results.add("Unsorted");
			
		}
		
		long endTime2 = System.currentTimeMillis();
		totalTimeSeconds = (endTime2 - startTime2) / 1000.0;

		results.add("Total Time (seconds): ");
		
		results.add(String.valueOf(totalTimeSeconds));
		
		results.add("Comparsion times:");
		
		results.add(String.valueOf(cp));
		
		cp = 0;
		try {
			report();
			System.out.println("Report written to pq_test.txt");
		} catch (IOException e) {
			System.out.println("Unable to write file comparisons.txt");
			return;
	}
		
	}	
	 public void report() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("pq_test.txt", false));
		for (String data : this.results)
			writer.append(data + " " + "\n");
		writer.close();
    }
   

    public static void main(String[] args)
    {
	    

		PQ225 tmp = new PQ225();
		tmp.test();
		
		
      
    }
}
