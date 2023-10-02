import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class BinarySearchDeluxe {

    // Returns the index of the first key in the sorted array a[]
    // that is equal to the search key, or -1 if no such key.
    // Reference: Elementary Sort slides.
    public static <Key> int firstIndexOf(Key[] a, Key key,
                                         Comparator<Key> comparator) {
        if (a == null) {
            throw new IllegalArgumentException("Null Array");
        }
        else if (key == null) {
            throw new IllegalArgumentException("Null Target");
        }
        else if (comparator == null) {
            throw new IllegalArgumentException("Null Comparator");
        }
        int min = -1; // Record the first index. -1 if no such key.
        int lo = 0, hi = a.length - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int compare = comparator.compare(key, a[mid]);
            if (compare < 0) hi = mid - 1;
            else if (compare > 0) lo = mid + 1;
            else {
                min = mid; // Key Found

                // Doing extra rounds of search to the left, until the lo<=hi
                hi = mid - 1;
            }
        }
        return min;
    }

    // Returns the index of the last key in the sorted array a[]
    // that is equal to the search key, or -1 if no such key.
    public static <Key> int lastIndexOf(Key[] a, Key key, Comparator<Key> comparator) {
        if (a == null) {
            throw new IllegalArgumentException("Null Array");
        }
        else if (key == null) {
            throw new IllegalArgumentException("Null Target");
        }
        else if (comparator == null) {
            throw new IllegalArgumentException("Null Comparator");
        }
        int max = -1; // Record the first index. -1 if no such key.
        int lo = 0, hi = a.length - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int compare = comparator.compare(key, a[mid]);
            if (compare < 0) hi = mid - 1;
            else if (compare > 0) lo = mid + 1;
            else {
                max = mid; // Key Found

                // Doing extra rounds of search to the right, until the lo<=hi
                lo = mid + 1;
            }
        }
        return max;
    }

    // unit testing (required)
    public static void main(String[] args) {
        String[] a = { "A", "A", "G", "G", "G", "T" };
        String[] b = { "A", "A", "A", "A", "A", "A" };
        String[] c = { "A" };
        String[] d = { "A", "A" };
        String[] e = { "A", "A", "A" };
        Comparator<String> comparator = String.CASE_INSENSITIVE_ORDER;
        int index = BinarySearchDeluxe.firstIndexOf(a, "G", comparator);
        StdOut.println(index); // should be 2.
        int index2 = BinarySearchDeluxe.lastIndexOf(a, "G", comparator);
        StdOut.println(index2); // should be 4.
        int index3 = BinarySearchDeluxe.firstIndexOf(b, "G", comparator);
        StdOut.println(index3); // should be -1.
        int index4 = BinarySearchDeluxe.lastIndexOf(b, "A", comparator);
        StdOut.println(index4); // should be 5.
        int index5 = BinarySearchDeluxe.firstIndexOf(c, "A", comparator);
        StdOut.println(index5); // should be 0.
        int index6 = BinarySearchDeluxe.lastIndexOf(c, "A", comparator);
        StdOut.println(index6); // should be 0.
        int index7 = BinarySearchDeluxe.firstIndexOf(d, "A", comparator);
        StdOut.println(index7); // should be 0.
        int index8 = BinarySearchDeluxe.lastIndexOf(d, "A", comparator);
        StdOut.println(index8); // should be 1.
        int index9 = BinarySearchDeluxe.firstIndexOf(e, "A", comparator);
        StdOut.println(index9); // should be 0.
        int index10 = BinarySearchDeluxe.lastIndexOf(e, "A", comparator);
        StdOut.println(index10); // should be 2.
        int index11 = BinarySearchDeluxe.firstIndexOf(a, "T", comparator);
        StdOut.println(index11); // should be 5.
        int index12 = BinarySearchDeluxe.lastIndexOf(a, "T", comparator);
        StdOut.println(index12); // should be 5.
    }
}
