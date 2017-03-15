
class WordListNode {
   
	String words;
    WordListNode next;

    WordListNode(String words) {
        this.words = words;
        this.next = null;
    }
	
	WordListNode(String words, WordListNode next) {
        this.words = words;
        this.next = next;
	}
}
