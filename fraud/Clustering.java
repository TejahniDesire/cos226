import edu.princeton.cs.algs4.DijkstraUndirectedSP;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.KruskalMST;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Clustering {

    private int[] clusters; // Quick Find array for clusters
    private int numOfClusters; // number of clusters
    private int m; // number of locations

    // run the clustering algorithm and create the clusters
    public Clustering(Point2D[] locations, int k) {
        // Corner cases
        if (locations == null)
            throw new IllegalArgumentException("Null Argument");
        for (int i = 0; i < locations.length; i++) {
            if (locations[i] == null)
                throw new IllegalArgumentException("Null Array Element");
        }
        if (k > locations.length || k < 1)
            throw new IllegalArgumentException("Invalid k Value");

        // number of locations
        numOfClusters = k;
        m = locations.length;

        // graph with vertices 0->m representing locations 0->m
        EdgeWeightedGraph tempLocationsGraph = new EdgeWeightedGraph(m);

        // Graph has m/2 * (m-1) edges, an edge for each pair
        for (int i = 0; i < m; i++) {
            for (int j = i + 1; j < m; j++) {
                // Create new edge between i and j
                // with weight = distance between vertex i and j
                Point2D point1 = locations[i];
                Point2D point2 = locations[j];
                double distance = point1.distanceTo(point2);
                Edge newEdge = new Edge(i, j, distance);
                tempLocationsGraph.addEdge(newEdge);

            }
        }

        // Store minimum spanning tree
        KruskalMST tempMinGraph = new KruskalMST(tempLocationsGraph);
        Edge[] minEdges = new Edge[m - 1];

        // Sort arrays in ascending order of weight
        int j = 0;
        for (Edge edges : tempMinGraph.edges()) {
            minEdges[j] = edges;
            j++;
        }
        Arrays.sort(minEdges);

        // Keep only the m - k edges
        EdgeWeightedGraph locationsClusters = new EdgeWeightedGraph(m); // Graph for all locations
        for (int i = 0; i < m - k; i++) {
            Edge thisEdge = minEdges[i];
            locationsClusters.addEdge(thisEdge);
            // StdOut.println(thisEdge);
        }

        // mark if vertex's cluster is already visited
        boolean[] vertexIsMarked = new boolean[m];

        // Traverse all clusters, marking vertices as of clusters (0-> k-1)
        int clusterNumber = 0;
        clusters = new int[m];
        for (int i = 0; i < m; i++) {
            if (!vertexIsMarked[i]) {
                vertexIsMarked[i] = true;
                if (locationsClusters.degree(i) == 0) {
                    clusters[i] = clusterNumber;
                }
                else {
                    clusters[i] = clusterNumber;
                    DijkstraUndirectedSP shortestPath =
                            new DijkstraUndirectedSP(locationsClusters, i);
                    for (int v = 0; v < m; v++) {
                        if (v != i && shortestPath.hasPathTo(v)) {
                            clusters[v] = clusterNumber;
                            vertexIsMarked[v] = true;
                        }
                    }
                    // for (Edge edges : locationsClusters.adj(i)) {
                    //     int otherVertex = edges.other(i);
                    //     vertexIsMarked[otherVertex] = true;
                    //     clusters[otherVertex] = clusterNumber;
                    // }
                }
                clusterNumber++;
            }
        }
    }

    // return the cluster of the ith point
    public int clusterOf(int i) {
        // Corner Cases
        if (i > m - 1 || i < 0)
            throw new IllegalArgumentException("Argument out of Bounds");
        
        return clusters[i];
    }

    // use the clusters to reduce the dimensions of an input
    public double[] reduceDimensions(double[] input) {
        // Corner Cases
        if (input == null)
            throw new IllegalArgumentException("Null Argument");
        if (input.length != m)
            throw new IllegalArgumentException("Null Argument");

        double[] reducedArray = new double[numOfClusters];
        for (int i = 0; i < input.length; i++) {
            reducedArray[clusterOf(i)] += input[i];
        }
        return reducedArray;
    }

    // unit testing (required)
    public static void main(String[] args) {
        In princtonLocationsText = new In("princeton_locations.txt");
        In transactionSummaryText = new In("princeton_locations_transaction_summary.txt");
        int amountOfClusters = 5;

        int numOfPoints = princtonLocationsText.readInt();
        double[] points = princtonLocationsText.readAllDoubles();
        Point2D[] princtonLocations = new Point2D[numOfPoints];
        double[] princtonTransactionSummary = transactionSummaryText.readAllDoubles();

        int k = 0;
        for (int i = 0; i < numOfPoints * 2; i += 2) {
            double x = points[i];
            double y = points[i + 1];
            princtonLocations[k] = new Point2D(x, y);
            // StdOut.println("New Point: " + princtonLocations[k]);
            k += 1;
        }
        Clustering test = new Clustering(princtonLocations, amountOfClusters);

        StdOut.println("Minimum members of Clusters-------------------------");
        StdOut.println("Cluster number for 0: " + test.clusterOf(0)); // 0
        StdOut.println("Cluster number for 1: " + test.clusterOf(1)); // 1
        StdOut.println("Cluster number for 6: " + test.clusterOf(6)); // 2
        StdOut.println("Cluster number for 11: " + test.clusterOf(11)); // 3
        StdOut.println("Cluster number for 20: " + test.clusterOf(20)); // 4

        StdOut.println("Random members of Clusters-------------------------");
        StdOut.println("Cluster number for 0: " + test.clusterOf(0)); // 0
        StdOut.println("Cluster number for 1: " + test.clusterOf(4)); // 1
        StdOut.println("Cluster number for 6: " + test.clusterOf(9)); // 2
        StdOut.println("Cluster number for 11: " + test.clusterOf(16)); // 3
        StdOut.println("Cluster number for 20: " + test.clusterOf(20)); // 4

        double[] reducedArray = test.reduceDimensions(princtonTransactionSummary);
        for (int i = 0; i < amountOfClusters; i++) {
            StdOut.println("Cluster i reduced sum : " + reducedArray[i]);
            // 5, 26, 24, 39, 7
        }

    }
}
