public class StringStackRefBased implements StringStack{
	
	private StringNode head = null;
	int size;
	int maxsize;
    
	public StringStackRefBased(int maxsize){
		this.maxsize=maxsize;
	}
	
	public boolean isEmpty(){
    return head == null;
	}
	
	public void push(String item) throws StringStackException{
		if(size+1>maxsize){
			throw new StringStackException("no room");
		}
		StringNode oldHead = head;
		head = new StringNode(item);
		head.next = oldHead;
		size++;
	}

    public String pop() throws StringStackException {
        if (isEmpty()) {
			throw new StringStackException("Empty Stack");
			}
		String result = head.item;
        head = head.next;
		size--;
        return result;
	}
    
	public void popAll(){
		head=null;
		size=0;
		
	}
	
	
    public String peek() throws StringStackException {
		if (isEmpty()) {
			throw new StringStackException("Stack underflow");
		}
		return head.item;
    }

	
	public static void main(String[] args) throws StringStackException{
		StringStackRefBased stack = new StringStackRefBased(10);
		boolean    passed;
		
		//Test isEmpty
		String t1 = "Test1";
        passed = stack.isEmpty();
        System.out.println(t1 + ": " +(passed ? "passed" : "FAILED"));
		
		//Test isEmpty after push()
		String t2 = "Test2";
		stack.push("hi");
		passed = !stack.isEmpty();
		System.out.println(t2 + ": " +(passed ? "passed" : "FAILED"));
		
		//Test isEmpty after push anything then pop
		String t3 = "Test3";
		stack.pop();
		passed = stack.isEmpty();
        System.out.println(t3 + ": " +(passed ? "passed" : "FAILED"));
		
		//Test isEmpty after push anything then peek
		String t4 = "Test4";
		stack.push("hi");
		stack.peek();
		passed = !stack.isEmpty();
        System.out.println(t4 + ": " +(passed ? "passed" : "FAILED"));
		stack.pop();
		
		//push three then pop three: check results of each pop for correctness
		stack.push("hi");
		stack.push("hello");
		stack.push("hey");
		stack.pop();
		if("hello" != stack.peek()){
			passed = false;      
		}
		
		stack.pop();
		if("hi" != stack.peek()){
			passed = false;
              
		}
		
		stack.pop();
		if(!stack.isEmpty()){
			passed = false;
		}
		
		if (passed) {
            System.out.println("Test5: passed");
        } else {
            System.out.println("Test5: FAILED");
        }
		
		//push three then peek three: only last pushed value should be returned three times
		String t6 = "Test6";
		stack.push("hi");
		stack.push("hello");
		stack.push("hey");
		stack.peek();
		stack.peek();
		stack.peek();
		if("hey" == stack.peek()){
			System.out.println(t6 + ": " +(passed ? "passed" : "FAILED"));
              
		}
		stack.pop();
		stack.pop();
		stack.pop();
		
		//push three then pop two: isEmpty should return false
		String t7 = "Test7";
		stack.push("hi");
		stack.push("hello");
		stack.push("hey");
		stack.pop();
		stack.pop();
		passed = !stack.isEmpty();
		System.out.println(t7 + ": " +(passed ? "passed" : "FAILED"));
		stack.pop();
		
		//push three then popAll: isEmpty should be true
		String t8 = "Test8";
		stack.push("hi");
		stack.push("hello");
		stack.push("hey");
		stack.popAll();
		passed = stack.isEmpty();
		System.out.println(t8 + ": " +(passed ? "passed" : "FAILED"));
		
		//Test pop stack: exception should be thrown
		String t5 = "Test5";
		passed = false;
		try{
			stack.pop();
		} catch (StringStackException e){
			passed = true;
		}
		if (passed) {
            System.out.println("Test9: passed");
        } else {
            System.out.println("Test9: FAILED");
        }
		
		
		
	}
}

