public class TaskListTester {

	
	public static void main(String args[]) {
		TaskList tl = new TaskListRefBased();
		int result;
		boolean passed = true ;
		
		
		//Test behavior of isEmpty
		if (tl.isEmpty()) {
            System.out.println("Test 1 (isEmpty): passed");
        } else {
            System.out.println("Test 1 (isEmpty): FAILED");
        }
		
		
		// Test behavior of length
		 if (tl.getLength() == 0) {
            System.out.println("Test 2 (length): passed");
        } else {
            System.out.println("Test 2 (length): FAILED");
        }
		
		int expected[] = {212, 198, 104};
		
		tl.insert(new Task(10, 212));
		tl.insert(new Task(12, 100));
		tl.insert(new Task(10, 198));
		tl.insert(new Task(3, 104));
        
		
		// Test behavior of length after adding 4 task
        if (tl.getLength() == 4) {
            System.out.println("Test 3 (length): passed");
        } else {
            System.out.println("Test 3 (length): FAILED");
        }
        
		Task head = tl.removeHead();
		
		
		// Test behavior of length after removeHead task
		if (tl.getLength() == 3) {
            System.out.println("Test 4 (length): passed");
        } else {
            System.out.println("Test 4 (length): FAILED");
        }
		
		
		// Test behavior of retrieve of the single item
		result = tl.retrieve(0).number;
        if (result == 212) {
            System.out.println("Test 5 (retrieve): passed");
        } else {
            System.out.println("Test 5 (retrieve): FAILED");
        }
		
		
		// Test after removeHead, Is the t.number equal to 212,198,104?
		if (head.number != 100) {
			passed = false;
		}
		for (int i = 0; i < expected.length ; i++) {
			Task t = tl.retrieve(i);
		
			if (t != null && t.number != expected[i]) {
				passed = false;
				break;

			} else if (t == null) {
				passed = false;
				break;
			}
		}
		if (passed) {
			System.out.println("Test 6 (removeHead): passed");
		} else {
			System.out.println("Test 6 (removeHead): FAILED");
			}
		
		
		// Test behavior when insert (10,250) into list, The expected list should be 212,198,250,104.
		tl.insert(new Task(10, 250));
		int expected1[] = {212, 198, 250, 104};
		for (int i = 0; i < expected1.length ; i++) {
			Task t = tl.retrieve(i);
		
			if (t != null && t.number != expected1[i]) {
				passed = false;
				break;

			} else if (t == null) {
				passed = false;
				break;
			}
		}
		if (passed) {
			System.out.println("Test 7 (insert): passed");
		} else {
			System.out.println("Test 7 (insert): FAILED");
			}
		
		
		//Test behavior when remove (10,250) from list, The expected list should be 212,198,104.
		tl.remove(new Task(10, 250));
		int expected2[] = {212, 198, 104};
		for (int i = 0; i < expected2.length ; i++) {
			Task t = tl.retrieve(i);
		
			if (t != null && t.number != expected2[i]) {
				passed = false;
				break;

			} else if (t == null) {
				passed = false;
				break;
			}
		}
		if (passed) {
			System.out.println("Test 8 (remove): passed");
		} else {
			System.out.println("Test 8 (remove): FAILED");
			}
		
		
		//Test behavior when remove head (10,212) from list, The expected list should be 198,104.
		tl.remove(new Task(10, 212));
		int expected3[] = {198, 104};
		for (int i = 0; i < expected3.length ; i++) {
			Task t = tl.retrieve(i);
		
			if (t != null && t.number != expected3[i]) {
				passed = false;
				break;

			} else if (t == null) {
				passed = false;
				break;
			}
		}
		if (passed) {
			System.out.println("Test 9 (remove head): passed");
		} else {
			System.out.println("Test 9 (remove head): FAILED");
			}
		
		
		//Test behavior when remove tail (3,104) from list, The expected list should be 198.
		tl.remove(new Task(3, 104));
		int expected4[] = {198};
		for (int i = 0; i < expected4.length ; i++) {
			Task t = tl.retrieve(i);
		
			if (t != null && t.number != expected4[i]) {
				passed = false;
				break;

			} else if (t == null) {
				passed = false;
				break;
			}
		}
		if (passed) {
			System.out.println("Test 10 (remove tail): passed");
		} else {
			System.out.println("Test 10 (remove tail): FAILED");
			}
		
}
}
