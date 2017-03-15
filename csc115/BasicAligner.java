/**
 * NullAligner.java 
 * Created for CSC115 Assignment One.
 *
 * This aligner does nothing, really, but it is really meant to
 * act as code to permit SeqAligner.java to compile.
 */
import java.util.ArrayList;
import java.util.List;
public class BasicAligner implements Aligner {
	
	private String[] sequence;
	private char[][] asChars;
    private int numErrors;
	private int position;
	private int min;
	private int min2;
    public BasicAligner(String sequence[]) { 
	    this.sequence = sequence;
		asChars = new char[sequence.length][];
		
		for(int i=0; i < sequence.length ; i++){
			asChars[i] = sequence[i].toCharArray();
		}
	    numErrors = -1;
	}
    private int matchAt(int firstI, int secondI){
		int numErrors = 0;
		for(int i=0; i < sequence[1].length() ; i++) {
			if(asChars[0][i + firstI] != asChars[1][i]) {
			    numErrors ++;	
			}
		}
		return numErrors;
	}

    public void performAlignment() {
		List<Integer> list1 = new ArrayList<Integer>();
		for(int j=0; j<sequence[0].length()-sequence[1].length();j++){
			list1.add(matchAt(j,0));
			}
		int min=list1.get(0);
		for(int i=0; i<list1.size();i++){
			if (min>list1.get(i)){
				min=list1.get(i);
			}
        }
		position = list1.indexOf(min);
		min2 = min;
    }


    public int getOffset(int sequenceNumber) {
        return position;
    }


    public String getSequence(int sequenceNumber) {
		String str1 = sequence[0];
		String str2 = sequence[1];
		if (sequenceNumber == 0) {
			return str1;
		} else {
			return str2;
		}
	}


    public int getNumErrors() {
        return min2;
    }
    public static void main(String[] args) {
		
		String[] tests = {"AAAGGCA","GGC"};
		BasicAligner ba = new BasicAligner(tests);
		System.out.println(ba.matchAt(4,0));
		
	}
	
	
}




