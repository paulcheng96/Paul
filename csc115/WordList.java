public class WordList {
    WordListNode  head;
    WordListNode  tail;
	public WordList() {
        head = null;
    }


    public int getLength() {
        int count = 0;
		
		for(WordListNode t = head; t != null; t = t.next){
			count++;
		}
        return count;
	}
    
    public void insertHead(String w){
		WordListNode t = new WordListNode(w);
		t.next = head;
		head = t;
	}


	public boolean contains(String w){
		for(WordListNode t = head; t != null; t = t.next){
			if (t.words.equals(w)){
				return true;
			}
		}
		return false;
	}
	
    public boolean isEmpty() {
        
		
        return (head==null);
    }



    public void  removeHead() {
        WordListNode prev = null;
		WordListNode t = head;
		
		if (t == null){
            return;  
		}
        else {
              if (t == tail) {
                    prev = t;
					head = null;
                    tail = null;
					
              } else {
                    prev = t;
					head = t.next;
					
              }
        }
	
		
    }
    
    public String retrieve(int i) {
		if (i > getLength()) {
            return null;
        }
        
		int n = 0;
		WordListNode curr = head;
		while (curr != null) {
            if (i > n) {
                curr = curr.next;
				n++;
            } else {
                break;
            }
        }

        return curr.words;
    }

    public void displayAllNodes() {  
        for (WordListNode curr = head; curr != null; curr = curr.next) {
			System.out.println(curr.words);
} 
     }

    public static void main(String[] args){
		WordList t1 = new WordList();
		boolean pass = true;
		
		//Test isEmpty;
		if (t1.isEmpty()) {
            System.out.println("Test 1 (isEmpty): passed");
        } else {
            System.out.println("Test 1 (isEmpty): FAILED");
        }
		
		// Test behavior of length
		if (t1.getLength() == 0) {
            System.out.println("Test 2 (length): passed");
        } else {
            System.out.println("Test 2 (length): FAILED");
        }
		
		t1.insertHead("lol");
		t1.insertHead("fine");
		t1.insertHead("good");
		t1.insertHead("damn");
		
		
		
		
		// Test behavior of length after adding 4 task
        if (t1.getLength() == 4) {
            System.out.println("Test 3 (length): passed");
        } else {
            System.out.println("Test 3 (length): FAILED");
        }
		
		t1.removeHead();
		
		// Test behavior of length after removeHead task
		if (t1.getLength() == 3) {
            System.out.println("Test 4 (length): passed");
        } else {
            System.out.println("Test 4 (length): FAILED");
        }
		
		// Test behavior of retrieve of the single item
		;
        if (t1.retrieve(0).equals("good")) {
            System.out.println("Test 5 (retrieve): passed");
        } else {
            System.out.println("Test 5 (retrieve): FAILED");
        }
		
		// Test behavior of retrieve of the contains
		if (t1.contains("lol")) {
            System.out.println("Test 6 (contains): passed");
        } else {
            System.out.println("Test 6 (contains): FAILED");
        }
		
		t1.removeHead();
		// Test behavior of retrieve of the contains after removeHead
		if (t1.contains("good")) {
            System.out.println("Test 7 (contains): FAILED");
        } else {
            System.out.println("Test 7 (contains): passed");
        }
		
		//Test isEmpty after adding nodes
		if (t1.isEmpty()) {
            System.out.println("Test 8 (isEmpty): FAILED");
        } else {
            System.out.println("Test 8 (isEmpty): passed");
        }
		
		
		
	}	 

}
