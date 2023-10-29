import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

public class KdTreeST<Value> {
    private Node root; // Root of the Kd-tree
    private int size; // Size of the Kd-tree

    private class Node {
        private Point2D p; // The point (location).
        private Value val; // The value.
        private Node left, right; // The left and right sub-Node on the tree.
        private RectHV rect; // the Rectangle where the Node is located.

        // Construct the Node
        public Node(Point2D p, Value val, RectHV rect) {
            this.p = p;
            this.val = val;
            this.rect = rect;
        }
    }


    // construct an empty symbol table of points
    public KdTreeST() {
    }


    // is the symbol table empty?
    public boolean isEmpty() {
        return (root == null);
    }

    // number of points
    public int size() {
        return size;
    }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if (p == null) throw new IllegalArgumentException("Illegal Argument");
        if (val == null) throw new IllegalArgumentException("Illegal Argument");

        // Define the root Rectangle
        RectHV rootrect = new RectHV(Double.NEGATIVE_INFINITY,
                                     Double.NEGATIVE_INFINITY,
                                     Double.POSITIVE_INFINITY,
                                     Double.POSITIVE_INFINITY);

        // put the root
        root = putVertical(root, p, val, rootrect);
    }

    // put node in even levels of the KdTree
    private Node putVertical(Node x, Point2D p, Value val, RectHV rect) {
        // Insert if new
        if (x == null) {
            size++;
            return new Node(p, val, rect);
        }

        // If x's point is equal to p, then update the value.
        if (x.p.equals(p)) {
            x.val = val;
            return x;
        }
        // If the parent node x is in a vertical level, then its child is in a
        // horizontal level.
        // To the left subtree
        if (p.x() < x.p.x()) {
            RectHV temp = new RectHV(x.rect.xmin(), x.rect.ymin(),
                                     x.p.x(), x.rect.ymax());
            x.left = putHorizontal(x.left, p, val, temp);
        }
        else {
            // To the right subtree
            RectHV temp = new RectHV(x.p.x(), x.rect.ymin(), x.rect.xmax(),
                                     x.rect.ymax());
            x.right = putHorizontal(x.right, p, val, temp);
        }
        return x;
    }

    // put node in odd levels of the KdTree
    private Node putHorizontal(Node x, Point2D p, Value val, RectHV rect) {
        // Insert if new
        if (x == null) {
            size++;
            return new Node(p, val, rect);
        }

        // If x's point is equal to p, then update the value.
        if (x.p.equals(p)) {
            x.val = val;
            return x;
        }

        // If the parent node x is in a horizontal level, then its child is in a
        // vertical level.
        // To the left (bottom) subtree
        if (p.y() < x.p.y()) {
            RectHV temp = new RectHV(x.rect.xmin(), x.rect.ymin(),
                                     x.rect.xmax(), x.p.y());
            x.left = putVertical(x.left, p, val, temp);
        }
        else {
            // To the right (above) subtree
            RectHV temp = new RectHV(x.rect.xmin(), x.p.y(), x.rect.xmax(),
                                     x.rect.ymax());
            x.right = putVertical(x.right, p, val, temp);
        }
        return x;
    }


    // value associated with point p
    public Value get(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Illegal Argument");
        Node x = root;
        int level = 0; // Level on the KdTree
        while (x != null) {
            if (x.p.equals(p)) return x.val; // Found. Return.

            // Currently Vertical
            if (level % 2 == 0) {
                if (p.x() < x.p.x()) x = x.left;
                else x = x.right;
            }
            else { // Currently Horizontal
                if (p.y() < x.p.y()) x = x.left;
                else x = x.right;
            }
            level++;
        }
        return null;
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Illegal Argument");
        return (get(p) != null);
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        Queue<Node> queue1 = new Queue<>();
        Queue<Point2D> queue2 = new Queue<>();
        queue1.enqueue(root);
        while (!queue1.isEmpty()) {
            Node current = queue1.dequeue();
            queue2.enqueue(current.p);
            if (current.left != null) {
                queue1.enqueue(current.left);
            }
            if (current.right != null) {
                queue1.enqueue(current.right);
            }
        }
        return queue2;
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        return new Queue<>();

    }

    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        return p;

    }


    // unit testing (required)
    public static void main(String[] args) {
        KdTreeST<Integer> test = new KdTreeST<>();
        StdOut.println(test.isEmpty()); // True
        Point2D point1 = new Point2D(0.7, 0.2);
        Point2D point2 = new Point2D(0.5, 0.4);
        Point2D point3 = new Point2D(0.2, 0.3);
        Point2D point4 = new Point2D(0.4, 0.7);
        Point2D point5 = new Point2D(0.9, 0.6);
        test.put(point1, 1);
        StdOut.println(test.isEmpty()); // False
        test.put(point2, 2);
        test.put(point3, 3);
        test.put(point4, 4);
        test.put(point5, 5);
        StdOut.println(test.size()); // 5
        StdOut.println(test.contains(point3)); // True
        Point2D point6 = new Point2D(0.4, 0.65);
        StdOut.println(test.contains(point6)); // False
        StdOut.println(test.get(point2)); // 2
        Iterable<Point2D> temp = test.points();
        for (Point2D i : temp) {
            StdOut.println(i.toString()); // See Assignment 4 Website Example
        }
    }
}
