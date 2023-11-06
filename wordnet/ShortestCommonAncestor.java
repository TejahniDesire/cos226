import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Topological;

public class ShortestCommonAncestor {
    private Digraph G; // Defensive copy of the input
    private int root; // The root vertex
    private int length; // Length
    private int ancestor; // Ancestor

    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G) {
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


    // length of shortest ancestral path between v and w
    /*
    The idea is to only search for
     */
    public int length(int v, int w) {
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(G, w);
        length = bfsv.distTo(v) + bfsw.distTo(v);
        lenancSearch(v, bfsv, bfsw);
        return length;
    }

    // Recursive search method for shortest length and ancestor
    private void lenancSearch(int v, BreadthFirstDirectedPaths bfsv,
                              BreadthFirstDirectedPaths bfsw) {
        boolean[] marked = new boolean[G.V()];
        // Check for itself first
        int dist;
        if (!bfsw.hasPathTo(v)) dist = Integer.MAX_VALUE;
        else dist = bfsv.distTo(v) + bfsw.distTo(v);
        if (dist < length) {
            length = dist;
            ancestor = v;
        }

        // Then check all adj.
        for (int k : G.adj(v)) {
            if (marked[k]) continue;
            else marked[k] = true;
            if (!bfsw.hasPathTo(k)) dist = Integer.MAX_VALUE;
            else {
                dist = bfsv.distTo(k) + bfsw.distTo(k);
            }
            if (dist < length) {
                length = dist;
                ancestor = k;
            }
            if (k != root) lenancSearch(k, bfsv, bfsw);
        }
    }

    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(G, w);
        length = bfsv.distTo(v) + bfsw.distTo(v); // Update length
        lenancSearch(v, bfsv, bfsw);
        return ancestor;
    }

    /*
    // length of shortest ancestral path of vertex subsets A and B
    public int lengthSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {

    }

    // a shortest common ancestor of vertex subsets A and B
    public int ancestorSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {

    }

     */

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
        StdOut.println(temp.ancestor(0, 3)); //
    }

}
