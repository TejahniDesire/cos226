import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class Term implements Comparable<Term> {

    private long tWeight; // Term Weight
    private String tQuery; // Term Query

    // Initializes a term with the given query string and weight.
    public Term(String query, long weight) {
        tQuery = query;
        tWeight = weight;
    }

    // Compares the two terms in descending order by weight.
    public static Comparator<Term> byReverseWeightOrder() {
        return new ReverseWeightOrderCompare();
    }

    private static class ReverseWeightOrderCompare implements Comparator<Term> {

        public int compare(Term t1, Term t2) {
            return Float.compare(t1.tWeight, t2.tWeight);
        }
    }
/*
    // Compares the two terms in lexicographic order,
    // but using only the first r characters of each query.
    public static Comparator<Term> byPrefixOrder(int r) {

    }*/

    // Compares the two terms in lexicographic order by query.
    public int compareTo(Term that) {
        // Compares this.query to that.query via built in string comparison
        return this.tQuery.compareTo(that.tQuery);
    }

    // Returns a string representation of this term in the following format:
    // the weight, followed by a tab, followed by the query.
    public String toString() {
        return "" + this.tWeight + "\t" + this.tQuery;

    }

    // unit testing (required)
    public static void main(String[] args) {
        Term[] array = new Term[3];
        array[0] = new Term("AA", 3);
        array[1] = new Term("AB", 2);
        array[2] = new Term("AC", 1);
        for (int i = 0; i < array.length; i++)
            StdOut.println(array[i]);

        StdOut.println("Sorting by weight");

        Arrays.sort(array, Term.byReverseWeightOrder());
        for (int i = 0; i < array.length; i++)
            StdOut.println(array[i]);

        StdOut.println("Sorting by lexicon");
        Arrays.sort(array);
        for (int i = 0; i < array.length; i++)
            StdOut.println(array[i]);
    }

}
