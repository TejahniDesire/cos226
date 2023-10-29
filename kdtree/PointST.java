import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.StdOut;

public class PointST<Value> {

    private RedBlackBST<Point2D, Value> pointsBST;

    // construct an empty symbol table of points
    public PointST() {
        pointsBST = new RedBlackBST<Point2D, Value>();
    }

    // is the symbol table empty?
    public boolean isEmpty() {
        return (pointsBST.size() == 0);
    }

    // number of points
    public int size() {
        return pointsBST.size();
    }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if (p == null || val == null)
            throw new IllegalArgumentException("NUll ARGUMENT");

        pointsBST.put(p, val);
        // If key already in, overwrites
    }

    // value associated with point p
    public Value get(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("NUll ARGUMENT");

        return pointsBST.get(p);
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("NUll ARGUMENT");

        return pointsBST.contains(p);
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        return pointsBST.keys();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("NUll ARGUMENT");
        
        RedBlackBST<Point2D, Value> withinRect = new RedBlackBST<Point2D, Value>();
        for (Point2D point : this.points()) {
            if (rect.contains(point))
                withinRect.put(point, this.get(point));
        }
        return withinRect.keys();
    }

    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {

        if (this.isEmpty())
            return null;

        double minDistance = Double.POSITIVE_INFINITY;
        Point2D champion = new Point2D(0, 0);

        for (Point2D point : this.points()) {
            double nextDistance = p.distanceSquaredTo(point);
            if (nextDistance < minDistance) {
                minDistance = nextDistance;
                champion = point;
            }
        }

        return champion;
    }

    // unit testing (required)
    public static void main(String[] args) {
        PointST<Integer> test = new PointST<Integer>();
        StdOut.println("Is the ST Empty? : " + test.isEmpty()); // True
        StdOut.println("Size of ST: " + test.size()); // 0


        Point2D apoint = new Point2D(0.52, 0.71);
        Point2D bpoint = new Point2D(0.39, 0.54);
        Point2D cpoint = new Point2D(0.74, 0.11);
        Point2D dpoint = new Point2D(0.98, 0.28);
        Point2D epoint = new Point2D(1.98, 1.28);

        // False
        StdOut.println("Contains point (0.52, 0.71)? : " + test.contains(apoint));

        test.put(apoint, 0);
        test.put(bpoint, 0);
        test.put(cpoint, 0);
        test.put(dpoint, 0);
        test.put(epoint, 0);

        // True
        StdOut.println("Contains point (0.52, 0.71)? : " + test.contains(apoint));
        StdOut.println("Value of point (0.39, 0.54): " + test.get(bpoint)); // 0
        StdOut.println("Is the ST Empty? : " + test.isEmpty()); // False
        StdOut.println("Size of ST: " + test.size()); //  5

        // (0.74, 0.11)
        // (0.98, 0.28)
        // (0.39, 0.54)
        // (0.52, 0.71)
        // (1.98, 1.28)
        for (Point2D point : test.points()) {
            StdOut.println(point);
        }

        // rect testing
        StdOut.println("Distance Testing");
        RectHV testRect = new RectHV(0.0, 0.0, 1.0, 1.0);

        // (0.74, 0.11)
        // (0.98, 0.28)
        // (0.39, 0.54)
        // (0.52, 0.71)
        for (Point2D point : test.range(testRect)) {
            StdOut.println(point);
        }

        StdOut.println("nearest point to (1.98, 1.29): " + test.nearest(
                new Point2D(1.98, 1.29))); // (1.98, 1.28)
        StdOut.println("nearest point to (0.50, 0.70): " + test.nearest(
                new Point2D(0.50, 0.70))); // (0.52, 0.71)


    }

}
