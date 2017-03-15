///package SelectMedian;
import java.util.ArrayList;

/**
 *
 * @author Rahnuma Islam Nishat
 * January 20, 2017
 * CSC 226 - Spring 2017
 */
public class SelectMedian {

       
    public static int LinearSelect(int[] A, int k){
        if (A.length == 1){
			return A[0];
		}
		ArrayList LessSide = new ArrayList();
		ArrayList Equals = new ArrayList();
		ArrayList Greater = new ArrayList();
		int p = goodPivot(A);
		if (p==-1){
			return -1;
		}
		
		for (int i = 0; i < A.length; i++){
			if (A[i] < p){
				LessSide.add(A[i]);
			}
			if (A[i] == p){
				Equals.add(A[i]);
			}else{
				Greater.add(A[i]);
			}
		}
		int[] LessSide1 = new int[LessSide.size()];
		for(int j=0;j<LessSide.size();j++){  
            LessSide1[j]=(int)LessSide.get(j);  
        }  
		int[] Equals1 = new int[Equals.size()];
		for(int m=0;m<Equals.size();m++){  
            Equals1[m]=(int)Equals.get(m);  
        } 
		int[] Greater1 = new int[Greater.size()];
		for(int l=0;l<Greater.size();l++){  
            Greater1[l]=(int)Greater.get(l);  
        } 
		
		
		if (k<=LessSide.size()){
			return LinearSelect(LessSide1,k);
		}else if(k<=(LessSide.size()+Equals.size())){
			return p;
		}else{
			return LinearSelect(Greater1,k-(LessSide.size()+Equals.size()));
		}
		
    }
	
   
    
	private static int goodPivot(int[] A){
		int m = A.length;
		
		int len = 0;
		if (m%5 != 0){
			len =  m/5 +1;
		}else{
			len = m/5;
		}
		
		int[][] list = new int[len][5];
		int[] list1 = new int[len];
		int i = 0;
		for (int j = 0;j<len;j++){
			int y = 0;
			while (y<5&&y<m-j*5){
								
				list[j][y]=A[i];
				i++;
				y++;
			
			}
		}
		
		
		for (int n = 0; n < len; n++){
			list1[n] = getMedian(list[n]);
		}
		
		return getMedian(list1);
		
	} 
	
	private static int getMedian(int[] A){
		boolean swapped=true;
    	
    	int length = 0;
		for (int a=0; a<A.length;a++) {
			if (A[a]!=0){
				length++;
			}	
    	}
	
		int[] values= new int [length];
		for (int i=0; i<length;i++) {
			values[i]=A[i];
    	}
		
 
    	while (swapped) {
    		swapped=false;
    		for (int i=0;i<values.length-1;i++) {
    			if (values[i]>values[i+1]) {
    				swap(values,i,i+1);
    				swapped=true;
    			}
    		}
    	}
    	int median = values[(values.length-1)/2];
    	for (int i=0;i<5;i++) {
    		if (A[i]==median) {
    			return A[i];
    		}
    	}
    	return -1;
	}
    
	private static void swap(int[]array, int a, int b){
 		int tmp = array[a];
		array[a] = array[b];
		array[b] = tmp;
	}
	
    public static void main(String[] args) {
        int[] A = {50, 54, 49, 49, 48, 49, 56, 52, 51, 52, 50, 59};
        System.out.println("The median weight is " + LinearSelect(A, A.length/2));
    }
    
}