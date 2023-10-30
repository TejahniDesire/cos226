import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

public class runtime {
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);

        // initialize the two data structures with point from standard input
        PointST<Integer> brute = new PointST<Integer>();
        KdTreeST<Integer> kdtree = new KdTreeST<Integer>();
        for (int i = 0; !in.isEmpty(); i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.put(p, i);
            brute.put(p, i);
        }

        int trials = 10000000;
        Point2D[] points = new Point2D[trials];
        for (int i = 0; i < trials; i++) {
            points[i] = new Point2D(StdRandom.uniformDouble(),
                                    StdRandom.uniformDouble());
        }
        Stopwatch bruteTime = new Stopwatch();
        for (int i = 0; i < trials / 100000; i++)
            brute.nearest(points[i]);
        StdOut.println("Brute Time: " + bruteTime.elapsedTime());


        Stopwatch kdTime = new Stopwatch();
        for (int i = 0; i < trials; i++)
            kdtree.nearest(points[i]);
        StdOut.println("kdtree Time: " + kdTime.elapsedTime());
    }
}
