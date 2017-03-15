 public class Extra{
	  TaskListNode  head;
 //I wrote a public void displayAllNodes() to display all nodes, which can help us to find if 
 //function works well. Tester only shows if function works, but if function doesn't work well,
 //we can use display function to see where is wrong.
 public static void main(String[] args){
	TaskList tl = new TaskListRefBased();
	tl.insert(new Task(10, 212));
	tl.insert(new Task(12, 100));
	tl.insert(new Task(10, 198));
	tl.insert(new Task(3, 104));
	 
	tl.displayAllNodes();
 }
 
 

 }  	
