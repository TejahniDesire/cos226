import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Topological;

public class Test {
    public static void main(String[] args) {
        Digraph test = new Digraph(5);
        test.addEdge(0, 1);
        test.addEdge(0, 2);
        test.addEdge(1, 3);
        test.addEdge(2, 3);
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(test, 1);
        Iterable<Integer> adj = test.adj(0);
        for (int i : adj) {
            StdOut.println(i);
        }
    }
}
