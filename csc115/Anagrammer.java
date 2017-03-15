public class Anagrammer
{
    private String[] dictionary;
    private String phrase;
    private int maxWords;

    
    Anagrammer(String dictionary[], String phrase, int maxWords)
    {
        this.dictionary = dictionary;
        this.phrase = phrase;
        this.maxWords = maxWords;
    }

    public void generate()
    {
        WordList words = new WordList();
        AlphabetStats phraseStats = new AlphabetStats(this.phrase);
        findAnagram(words, phraseStats);
    }

    

    private void findAnagram(WordList words, AlphabetStats phraseStats)
    {        
        if (words.getLength() <= this.maxWords && phraseStats.isEmpty())
        {
            for (int i = words.getLength() - 1; i >= 0; i--)
            {
                System.out.print(words.retrieve(i));
                if (i != 0)
                    System.out.print(",");
            }
            System.out.println();
        } else
        {
            for (int i = 0; i < dictionary.length; i++)
            {
                AlphabetStats as = new AlphabetStats(dictionary[i]);
                
                if (phraseStats.contains(as))
                {                
                    words.insertHead(dictionary[i]);
                    phraseStats.subtract(as);
                    findAnagram(words, phraseStats);                                        
					words.removeHead();                    
					phraseStats.add(as);
                }
            }
        }
	}
	
	public static void main(String[] args)
	{
		
		//Test anagrammer for only one word
		System.out.println("Test1");
		String[] list8 = {"love"};
		Anagrammer t8 = new Anagrammer(list8, "love", 1);
		t8.generate();		
		System.out.println("");
		
		//Test anagrammer for two words
		System.out.println("Test2");
		String[] list1 = {"love","hi"};
		Anagrammer t1 = new Anagrammer(list1, "lovehi", 2);
		t1.generate();		
		System.out.println("");
		
		//Test anagrammer for three words
		System.out.println("Test3");
		String[] list2 = {"love","hi","hey"};
		Anagrammer t2 = new Anagrammer(list2, "lovehihey", 3);
		t2.generate();
	    System.out.println("");
			    
		//Test if maxWords less than number of words that should be found
	    String[] list3 = {"love","hi","hey"};
		Anagrammer t3 = new Anagrammer(list3, "lovehihey", 1);
		System.out.println("Test4");
		System.out.println("Expected output: ");
		System.out.println("null");
		System.out.println("Actual output: ");
		t3.generate();	
        System.out.println("");
		
        //Test if phrase is empty
	    String[] list4 = {"love","hi","hey"};
		Anagrammer t4 = new Anagrammer(list4, "", 3);
		System.out.println("Test5");
		System.out.println("Expected output: ");
		System.out.println("null");
		System.out.println("Actual output: ");
		t4.generate();			
	
	    //Test anagrammer  for four words
	    System.out.println("Test6");
		String[] list5 = {"love","hi","hey","bye"};
		Anagrammer t5 = new Anagrammer(list5, "lovehiheybye", 4);	
		t5.generate();	
	    
		//Test anagrammer  for five words
	    System.out.println("Test7");
		String[] list6 = {"love","hi","hey","bye","if"};
		Anagrammer t6 = new Anagrammer(list6, "lovehiheybyeif", 5);	
		t6.generate();	
	    
	    //Test anagrammer  for empty dictionary
		String[] list7 = {};
		Anagrammer t7 = new Anagrammer(list7, "lovehiheybyeif", 5);	
		System.out.println("Test8");
		System.out.println("Expected output: ");
		System.out.println("null");
		System.out.println("Actual output: ");
		t7.generate();
	
	    
	}
}