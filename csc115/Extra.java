public class Extra {
	private BSTRefBased t;
	
	//This method will return how many node in the current tree
	public int GetNodeNum(TreeNode node){
		if(node==null){
			return 0;
		}
		return GetNodeNum(node.left) + GetNodeNum(node.right) + 1;
	}
	
	//This method will return the height of the current tree
	public int GetDepth(TreeNode node){
		if(node==null){
			return 0;
		}
		int depthleft = GetDepth(node.left);
		int depthright = GetDepth(node.right);
		return depthleft > depthright ? (depthleft + 1) : (depthright + 1);  
	}
	
	public static void main(String[] args){
		BSTRefBased t;
		Extra tt = new Extra();
		boolean result;
		String message;
		
		message = "Test 1: inserting 'humpty' -- ";
        t = new BSTRefBased();
        try {
            t.insert("humpty");
            result = tt.GetNodeNum(t.getRoot())==1;
			result &=tt.GetDepth(t.getRoot())==1;
        } catch (Exception e) {
            result = false;
        }
        System.out.println(message + (result ? "passed" : "FAILED"));
		
		message = "Test 2: inserting 'humpty' and 'dumpty' -- ";
        t = new BSTRefBased();
        try {
            t.insert("humpty");
			t.insert("dumpty");
            result = tt.GetNodeNum(t.getRoot())==2;
			result &=tt.GetDepth(t.getRoot())==2;
        } catch (Exception e) {
            result = false;
        }
        System.out.println(message + (result ? "passed" : "FAILED"));
	}
}