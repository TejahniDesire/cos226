import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Topological;

public class ShortestCommonAncestor {
    private Digraph G; // Defensive copy of the input
    private int root; // The root vertex
    private int length; // Length
    private int ancestor; // Ancestor
    private boolean[] marked; // Marked Vertex

    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G) {
        if (G == null) throw new IllegalArgumentException("Null Input");
        this.G = new Digraph(G); // Save the defensive copy.
        Topological test = new Topological(this.G);
        marked = new boolean[this.G.V()];

        // Test if G is a DAG
        if (!test.hasOrder()) {
            throw new IllegalArgumentException("Input is not a rooted DAG");
        }

        // Test if G has only one root.
        int rootCount = 0;
        Iterable<Integer> order = test.order();
        for (int i : order) {
            if (this.G.outdegree(i) == 0) {
                root = i;
                rootCount++;
            }
        }
        if (rootCount != 1) throw new
                IllegalArgumentException("Input is not a rooted DAG");
    }


    // length of shortest ancestral path between v and w
    /*
    The idea is starting from one of the two vertex, recursively calculate
    the length of ancestral paths that go through its reachable parents. Store
    the champion (both length and ancestor) in an instance variable.
     */
    public int length(int v, int w) {
        if (v < 0 || v >= this.G.V()) throw new
                IllegalArgumentException("Input out of range");
        if (w < 0 || w >= this.G.V()) throw new
                IllegalArgumentException("Input out of range");
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(G, w);
        length = bfsv.distTo(root) + bfsw.distTo(root);
        marked = new boolean[this.G.V()]; // clear marked
        lenancSearch(v, bfsv, bfsw);
        return length;
    }

    // Recursive search method for shortest length and ancestor
    private void lenancSearch(int v, BreadthFirstDirectedPaths bfsv,
                              BreadthFirstDirectedPaths bfsw) {
        // A marked array to avoid repeated calculations.


        // Check for itself first
        int dist;
        marked[v] = true;
        if (bfsw.hasPathTo(v)) {
            dist = bfsv.distTo(v) + bfsw.distTo(v);
            if (dist <= length) {
                length = dist;
                ancestor = v;
            }
        }

        if (v == root) return;

        // Then check all adj.
        for (int k : G.adj(v)) {
            if (marked[k]) continue;
            if (bfsw.hasPathTo(k)) {
                dist = bfsv.distTo(k) + bfsw.distTo(k);
                if (dist <= length) {
                    length = dist;
                    ancestor = k;
                }
            }
            lenancSearch(k, bfsv, bfsw); // Keep searching
        }

    }

    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
        if (v < 0 || v >= this.G.V()) throw new
                IllegalArgumentException("Input out of range");
        if (w < 0 || w >= this.G.V()) throw new
                IllegalArgumentException("Input out of range");
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(G, w);
        marked = new boolean[this.G.V()]; // clear marked
        length = bfsv.distTo(root) + bfsw.distTo(root); // Update length
        lenancSearch(v, bfsv, bfsw);
        return ancestor;
    }


    // length of shortest ancestral path between subset v and w
    /*
    The idea is starting from one of the two vertex, recursively calculate
    the length of ancestral paths that go through its reachable parents. Store
    the champion (both length and ancestor) in an instance variable.
     */
    // length of shortest ancestral path of vertex subsets A and B
    public int lengthSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if (subsetA == null) throw new IllegalArgumentException("Null Input");
        if (subsetB == null) throw new IllegalArgumentException("Null Input");
        BreadthFirstDirectedPaths bfsa = new BreadthFirstDirectedPaths(G, subsetA);
        BreadthFirstDirectedPaths bfsb = new BreadthFirstDirectedPaths(G, subsetB);
        length = bfsa.distTo(root) + bfsb.distTo(root); // Update length
        marked = new boolean[this.G.V()]; // Update marked
        // Iterate through subsetA
        for (int i : subsetA) {
            lenancSearch(i, bfsa, bfsb);
        }
        return length;
    }


    // a shortest common ancestor of vertex subsets A and B
    public int ancestorSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if (subsetA == null) throw new IllegalArgumentException("Null Input");
        if (subsetB == null) throw new IllegalArgumentException("Null Input");
        BreadthFirstDirectedPaths bfsa = new BreadthFirstDirectedPaths(G, subsetA);
        BreadthFirstDirectedPaths bfsb = new BreadthFirstDirectedPaths(G, subsetB);
        length = bfsa.distTo(root) + bfsb.distTo(root); // Update length
        marked = new boolean[this.G.V()]; // Update marked
        // Iterate through subsetA
        for (int i : subsetA) {
            lenancSearch(i, bfsa, bfsb);
        }
        return ancestor;
    }



    // unit testing (required)
    public static void main(String[] args) {
        Digraph test = new Digraph(7);
        test.addEdge(0, 1);
        test.addEdge(0, 2);
        test.addEdge(1, 3);
        test.addEdge(2, 3);
        test.addEdge(3, 5);
        test.addEdge(4, 6);
        test.addEdge(5, 6);
        ShortestCommonAncestor temp = new ShortestCommonAncestor(test);
        StdOut.println(temp.root); // 6
        StdOut.println(temp.length(1, 2)); // 2
        StdOut.println(temp.ancestor(1, 2)); // 3
        StdOut.println(temp.ancestor(0, 3)); // 3
        Stack<Integer> temp1 = new Stack<Integer>();
        temp1.push(1);
        temp1.push(0);
        Stack<Integer> temp2 = new Stack<Integer>();
        temp2.push(4);
        temp2.push(3);
        StdOut.println(temp.lengthSubset(temp1, temp2)); // 1
        StdOut.println(temp.ancestorSubset(temp1, temp2)); // 3
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
