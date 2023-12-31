import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class Term implements Comparable<Term> {

    private long weight; // Term weight
    private String query; // Term query

    // Initializes a term with the given query string and weight.
    public Term(String query, long weight) {
        if (query == null) {
            throw new IllegalArgumentException("Null Query");
        }
        else if (weight < 0) {
            throw new IllegalArgumentException("Negative weight");
        }
        this.query = query;
        this.weight = weight;
    }

    // Compares the two terms in descending order by weight.
    public static Comparator<Term> byReverseWeightOrder() {
        return new ReverseWeightOrderCompare();
    }

    // Private class to be used in sorting by descending weight
    private static class ReverseWeightOrderCompare implements Comparator<Term> {

        public int compare(Term t1, Term t2) {
            // Using if conditions to avoid negate -2^31.
            if (Float.compare(t1.weight, t2.weight) > 0) return -1;
            else if (Float.compare(t1.weight, t2.weight) < 0) return 1;
            return 0;
        }
    }

    // Compares the two terms in lexicographic order,
    // but using only the first r characters of each query.
    public static Comparator<Term> byPrefixOrder(int r) {
        if (r < 0) throw new IllegalArgumentException("Negative r");
        return new PrefixOrderCompare(r);
    }

    // Private class to be used in sorting by first r letters
    private static class PrefixOrderCompare implements Comparator<Term> {
        private int r; // position up to which compare term queries

        // Initialize r value
        public PrefixOrderCompare(int r) {
            this.r = r;
        }

        public int compare(Term t1, Term t2) {
            String s1 = t1.query; // String 1, from term 1
            String s2 = t2.query; // String 2, from term 2

            // Compare the first r characters if both strings' lengths are
            // greater than r.
            if (s1.length() >= this.r && s2.length() >= this.r) {
                for (int i = 0; i < this.r; i++) {
                    if (s1.charAt(i) == s2.charAt(i)) continue;
                    return (s1.charAt(i) - s2.charAt(i));
                }
                return 0; // If all equal, return 0.
            }


            // If either of the two strings has length less than r,
            // leave what it is and compare.
            return s1.compareTo(s2);

        }
    }

    // Compares the two terms in lexicographic order by query.
    public int compareTo(Term that) {
        // Compares this.query to that.query via built in string comparison
        return this.query.compareTo(that.query);
    }

    // Returns a string representation of this term in the following format:
    // the weight, followed by a tab, followed by the query.
    public String toString() {
        return "" + this.weight + "\t" + this.query;

    }

    // unit testing (required)
    public static void main(String[] args) {
        Term[] array = new Term[3];
        array[0] = new Term("AA", 1);
        array[2] = new Term("AB", 2);
        array[1] = new Term("BC", 3);

        StdOut.println("Inputed array");
        for (int i = 0; i < array.length; i++)
            StdOut.println(array[i]);

        StdOut.println("Sorting by lexicon");
        Arrays.sort(array);
        for (int i = 0; i < array.length; i++)
            StdOut.println(array[i]);

        StdOut.println("Sorting by descending weight");
        Arrays.sort(array, Term.byReverseWeightOrder());
        for (int i = 0; i < array.length; i++)
            StdOut.println(array[i]);

        StdOut.println("Sorting by first letter");
        Arrays.sort(array, byPrefixOrder(1));
        for (int i = 0; i < array.length; i++)
            StdOut.println(array[i]);

    }

}
