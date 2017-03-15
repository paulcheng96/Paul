public class TaskListRefBased implements TaskList{
    TaskListNode  head;
    TaskListNode  tail;
	public TaskListRefBased() {
        head = null;
    }


    public int getLength() {
        int count = 0;
		
		for(TaskListNode t = head; t != null; t = t.next){
			count++;
		}
        return count;
	}
    


    public boolean isEmpty() {
        
		
        return (head==null);
    }



    public Task removeHead() {
        TaskListNode prev = null;
		TaskListNode t = head;
		
		if (t == null){
              return null;
		}
        else {
              if (t == tail) {
                    prev = t;
					head = null;
                    tail = null;
					return prev.task;
              } else {
                    prev = t;
					head = t.next;
					return prev.task;
              }
        }
	
		
    }
    

    
    public Task remove(Task t) {
		TaskListNode prev = null;
		TaskListNode curr = head;
		
		
		while (curr != null) {
			
			if (t.number==curr.task.number){
				head  = curr.next;
			    prev = curr;
				break;
				
			}
			prev = curr;
			prev = prev.next;
			if (prev.task.number == t.number) {
				curr.next = prev.next;
				break;
			} else {
				curr = curr.next;
			}
		}
	
		return prev.task;
} 


    public void insert(Task t) {

		
		TaskListNode prev = null;
		TaskListNode curr = head;
		while (curr != null) {
			if (t.priority < curr.task.priority || t.priority == curr.task.priority ) {
				prev = curr;
				curr = curr.next;
			} else {
				break;
			}
		}
		
		TaskListNode newNode = new TaskListNode(t);
		newNode.next = curr;
		if (prev == null) {
			head = newNode;
		} else {
			prev.next = newNode;
        }
    } 
		
    


    public Task retrieve(int i) {
		if (i > getLength()) {
            throw new ListException("Index " + i + " out of bounds");
        }
        
		int n = 0;
		TaskListNode curr = head;
		while (curr != null) {
            if (i > n) {
                curr = curr.next;
				n++;
            } else {
                break;
            }
        }

        return curr.task;
    }

    public void displayAllNodes() {  
        for (TaskListNode curr = head; curr != null; curr = curr.next) {
			System.out.println(curr.task.number);
} 
     }  	

}
