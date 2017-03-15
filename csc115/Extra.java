import java.util.*;
public class Extra{
	ArrayList<String> list;
	
	public Extra() {
        list = new ArrayList<String>();
    }
	
	//Extra method for print the StringList
	public void PrintPostfix(StringList expr){
		for(int i=0;i<expr.getLength();i++){
			System.out.println(expr.retrieve(i));
		}
	}
	
	public void insertTail(String item) {
        list.add(item);
    }
	
	//Extra method to search elements in the ArrayList
	public boolean SearchElements(String expr){
		if (list.size() == 0) {
            return false;
		}else if(list.contains(expr)){
			return true;
		} else{
			return false;
		}
		
	}
	
	public static void main(String[] args) {
        Extra t1 = new Extra();
		boolean    passed;
        StringList expr = new StringList();
		
		String[] a1 = {"aaa", "bbb", "ccc", "ddd"};
        for (String s : a1) {
            t1.insertTail(s);
			expr.insertTail(s);
        }		
		if (t1.SearchElements("aaa")==true){
			System.out.println("Test1 Passed");
		}		
		t1.PrintPostfix(expr);
}
}