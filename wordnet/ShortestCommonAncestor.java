import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Topological;

public class ShortestCommonAncestor {
    private Digraph G; // Defensive copy of the input
    private int root; // The root vertex

    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G) {
        if (G == null) throw new IllegalArgumentException("Null Input");
        this.G = new Digraph(G); // Save the defensive copy.
        Topological test = new Topological(this.G);

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


    // Helper method that find ancestor and length at the same time.
    private int[] lenancSearch(BreadthFirstDirectedPaths bfsv,
                              BreadthFirstDirectedPaths bfsw) {
        // initialize length and ancestor.
        int length = bfsv.distTo(root) + bfsw.distTo(root);
        int ancestor = root;

        // Element zero is length, and one is ancestor.
        int[] result = new int[2];

        // Iterate through vertex to search for length and ancestor.
        for (int i = 0; i < G.V(); i++) {
            if (bfsv.hasPathTo(i) && bfsw.hasPathTo(i)) {
                int dist = bfsv.distTo(i) + bfsw.distTo(i);
                if (dist < length) {
                    length = dist;
                    ancestor = i;
                }
            }
        }
        result[0] = length;
        result[1] = ancestor;
        return result;
    }


    // length of shortest ancestral path between v and w
    public int length(int v, int w) {
        if (v < 0 || v >= this.G.V()) throw new
                IllegalArgumentException("Input out of range");
        if (w < 0 || w >= this.G.V()) throw new
                IllegalArgumentException("Input out of range");
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(G, w);
        int[] result = lenancSearch(bfsv, bfsw);
        return result[0];
    }

    // a shortest common ancestor of vertices v and w
    // The idea is: when
    public int ancestor(int v, int w) {
        if (v < 0 || v >= this.G.V()) throw new
                IllegalArgumentException("Input out of range");
        if (w < 0 || w >= this.G.V()) throw new
                IllegalArgumentException("Input out of range");
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(G, w);
        int[] result = lenancSearch(bfsv, bfsw);
        return result[1];
    }


    // length of shortest ancestral path of vertex subsets A and B
    public int lengthSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if (subsetA == null) throw new IllegalArgumentException("Null Input");
        if (subsetB == null) throw new IllegalArgumentException("Null Input");
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(G, subsetA);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(G, subsetB);
        int[] result = lenancSearch(bfsv, bfsw);
        return result[0];
    }


    // a shortest common ancestor of vertex subsets A and B
    public int ancestorSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if (subsetA == null) throw new IllegalArgumentException("Null Input");
        if (subsetB == null) throw new IllegalArgumentException("Null Input");
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(G, subsetA);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(G, subsetB);
        int[] result = lenancSearch(bfsv, bfsw);
        return result[1];
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
        /*
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
         */
    }
}

