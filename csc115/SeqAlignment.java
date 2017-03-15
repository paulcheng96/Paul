import java.util.*;
import java.io.*;

/**
 * Provided for CSC 115, Assignment 1 (2015 Summer)
 *
 * @author Mike Zastre (UVic Department of Computer Science)
 * @version 1
 */
public class SeqAlignment {
    private int position;
	private int min;
	private static final int DEFAULT_MAX_ERRORS = 6;

    public static void main(String args[]) {
        String  infileName = null;
        int     maxErrors  = DEFAULT_MAX_ERRORS;
        boolean verbose    = false;

        // Process the command-line options and arguments.
        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("--maxerrors")) {
                    maxErrors = Integer.parseInt(args[i+1]);
                } else if (args[i].equals("--file")) {
                    infileName = args[i+1];
                } else if (args[i].equals("--verbose")) {
                    verbose = true;
                }
            }

            if (infileName == null) {
                System.out.println("Usage: please provide an input filename");
                System.exit(1);
            }

            // If we reach this point in the code, we have all the
            // information needed to proceed with processing.

            Scanner infileScanner;


            infileScanner = new Scanner(new File(infileName));
            String sequence[] = readSequences(infileScanner);


            // **************************************************
            // THE NEXT STATEMENT IS THE ONLY LINE TO BE MODIFIED
            // AND ONLY THE RIGHT-HAND SIDE AT THAT (TO THE RIGHT
            // OF THE = SIGN). SORRY FOR SHOUTING.
            //
            Aligner a = new BasicAligner(sequence);
			a.performAlignment();
            if (a.getNumErrors() > maxErrors) {
                System.out.println("No match");
            } else {
                if (verbose) {
                    printAlignment(a);
                }
                System.out.println(a.getOffset(1));
            }
            System.out.println(a.getNumErrors());
		 } 
        catch (NumberFormatException e) {
            System.err.println("Need an integer after --maxerrors");
            System.exit(1);
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Probably not enough arguments provided.\n");
            System.exit(1);
        }
    }

    /**
     * Reads in the sequence strings contained within the input stream.
     * The first line of the file denotes the number of remaining lines
     * in the file -- one sequence/string per line. There is
     * no error-checking here, so we are assuming that the
     * input file is properly formatted.
     *
     * @param input Input stream containing sequence string values
     * @return Array of strings corresponding to sequences read in
     *         from input stream.
     */
    public static String[] readSequences(Scanner input)
    {
        String line = input.nextLine();
        int    numSequences = Integer.parseInt(line);

        String result[] = new String[numSequences];

        for (int i = 0; i < numSequences; i++) {
            result[i] = input.nextLine();
        }
        return result;
		
    }


    /**
     * Using the sequence strings in the aligner, produce a
     * visualization of the second string as aligned to the
     * first. Matching errors are represented with "x".
     *
     * @param a An instance of an Aligner class containing the
     *        strings that were aligned plus offset indicating
     *        how to shift along the second string in order
     *        to align it to the first. 
     */
    public static void printAlignment(Aligner a) {
        /* If alignment hasn't taken place, or if there is
         * no alignment possible, then don't proceed any further.
         */
        
		if (a.getOffset(0) == -1 || a.getOffset(1) == -1) {
            return;
        }
        
        char first[] = a.getSequence(0).toCharArray();
        char second[] = a.getSequence(1).toCharArray();

        System.out.println(a.getSequence(0));
		

        int offset = a.getOffset(1);
 
        for (int i = 0; i < offset; i++) {
            System.out.print(".");
        }
    
        for (int i = 0; i < second.length; i++) {
            if (second[i] == first[i + offset]) {
                System.out.print(second[i]);
            } else {
                System.out.print("x");
            }
        }

        for (int i = offset + second.length; i < first.length; i++) {
            System.out.print(".");
        }
        System.out.println();
    }

}
