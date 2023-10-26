import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;

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
        pointsBST.put(p, val);
        // If key already in, overwrites
    }

    // value associated with point p
    public Value get(Point2D p) {
        return pointsBST.get(p);
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        return pointsBST.contains(p);
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        return pointsBST.keys();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {

    }

    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {

    }

    // unit testing (required)
    public static void main(String[] args) {

    }

}
