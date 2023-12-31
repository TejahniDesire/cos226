import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Autocomplete {

    private Term[] terms; // array of lexicon ordered terms

    // Initializes the data structure from the given array of terms.
    public Autocomplete(Term[] terms) {
        if (terms == null)
            throw new IllegalArgumentException("Null Array");
        this.terms = new Term[terms.length];
        for (int i = 0; i < terms.length; i++) {
            if (terms[i] == null)
                throw new IllegalArgumentException("Null Argument");
            this.terms[i] = terms[i]; // Defensive copy
        }
        Arrays.sort(this.terms); // n * log(n) compares
    }

    // Returns all terms that start with the given prefix,
    // in descending order of weight.
    public Term[] allMatches(String prefix) {
        if (prefix == null)
            throw new IllegalArgumentException("Null Argument");

        Term key = new Term(prefix, 1);


        // Log(n) time
        int firstM = BinarySearchDeluxe.firstIndexOf(
                this.terms, key, Term.byPrefixOrder(prefix.length()));

        // If no such prefix found, return zero-length array.
        if (firstM == -1) return new Term[0];

        int lastM = BinarySearchDeluxe.lastIndexOf(
                this.terms, key, Term.byPrefixOrder(prefix.length()));

        // Number of matches
        int m = lastM - firstM + 1;

        Term[] matches = new Term[m]; // Storage for all matching terms
        // m time
        for (int i = firstM; i < firstM + m; i++) {
            matches[i - firstM] = this.terms[i];
        }

        // m * log(m) time
        Arrays.sort(matches, Term.byReverseWeightOrder());
        return matches;

    }

    // Returns the number of terms that start with the given prefix.
    public int numberOfMatches(String prefix) {
        if (prefix == null)
            throw new IllegalArgumentException("Null Argument");

        Term key = new Term(prefix, 1);

        // Call binary search on prefix and comparator for matching up to
        // prefix length. Log(n) time
        int firstM = BinarySearchDeluxe.firstIndexOf(
                this.terms, key, Term.byPrefixOrder(prefix.length()));
        int lastM = BinarySearchDeluxe.lastIndexOf(
                this.terms, key, Term.byPrefixOrder(prefix.length()));

        // If no such key found, return 0;
        if (firstM == -1) return 0;

        return lastM - firstM + 1;
    }

    // unit testing (required)
    public static void main(String[] args) {
        Term[] array = new Term[5];
        array[0] = new Term("AA", 1);
        array[1] = new Term("AB", 2);
        array[2] = new Term("BC", 3);
        array[3] = new Term("BC", 4);
        array[4] = new Term("AC", 5);

        Autocomplete test = new Autocomplete(array);
        StdOut.println(test.numberOfMatches("A"));

        StdOut.println("Printing all 'A' Terms");
        Term[] allMatches = test.allMatches("A");
        for (int i = 0; i < allMatches.length; i++)
            StdOut.println(allMatches[i]);

        StdOut.println("Printing all 'AB' Terms");
        Term[] allMatches2 = test.allMatches("AB");
        for (int i = 0; i < allMatches2.length; i++)
            StdOut.println(allMatches2[i]);


        StdOut.print("Begin Given Example Main Code \n");
        // read in the terms from a file
        String filename = args[0];
        In in = new In(filename);
        int n = in.readInt();
        Term[] terms = new Term[n];
        for (int i = 0; i < n; i++) {
            long weight = in.readLong();           // read the next weight
            in.readChar();                         // scan past the tab
            String query = in.readLine();          // read the next query
            terms[i] = new Term(query, weight);    // construct the term
        }

        // read in queries from standard input and print the top k matching terms
        int k = Integer.parseInt(args[1]);
        Autocomplete autocomplete = new Autocomplete(terms);
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            Term[] results = autocomplete.allMatches(prefix);
            StdOut.printf("%d matches\n", autocomplete.numberOfMatches(prefix));
            for (int i = 0; i < Math.min(k, results.length); i++)
                StdOut.println(results[i]);
        }
    }

}
