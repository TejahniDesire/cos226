import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {

    private Digraph digraph; // underlying digraph for WordNet

    // Stores Nouns and corresponding synsets
    private RedBlackBST<String, Queue<Integer>> synsetsPerNouns;

    // Stores Synsets and corresponding Nouns
    private RedBlackBST<Integer, Queue<String>> nounsPerSynset;

    // Shortest commmon ancestor data type
    private ShortestCommonAncestor sca;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        In synsetsText = new In(synsets);
        In hypernymText = new In(hypernyms);
        hypernymParser(hypernymText);
        synsetParser(synsetsText);
        sca = new ShortestCommonAncestor(digraph);

    }

    // Reads a hypernym text to creat a digraph
    // assume number of synsets in hypernym matches that in synsets
    private void hypernymParser(In hypernymText) {
        // ST to contain each synset and all its hypernym
        LinearProbingHashST<Integer, Queue<Integer>> hypST =
                new LinearProbingHashST<Integer, Queue<Integer>>();
        int synsetAmount = 0; // Count the number of synsets
        while (hypernymText.hasNextLine()) {
            // Current line of the hypernym text
            String[] currentHypArr = hypernymText.readLine().split(",");
            Queue<Integer> adjQueue = new Queue<Integer>();

            int synId = Integer.parseInt(currentHypArr[0]);
            for (int i = 1; i < currentHypArr.length; i++) {
                adjQueue.enqueue(Integer.valueOf(currentHypArr[i]));
            }
            hypST.put(synId, adjQueue);
            synsetAmount++;
            // digraph.addEdge(Integer.parseInt(currentHypArr[0]), );
        }
        digraph = new Digraph(synsetAmount);

        // Fill up the diagraph's adjacency list
        for (int i = 0; i < synsetAmount; i++)
            for (int adjecent : hypST.get(i))
                digraph.addEdge(i, adjecent);
    }

    // Reads a hypernym text to creat a digraph
    // assume number of synsets in hypernym matches that in synsets
    private void synsetParser(In synsetText) {
        // synsetsPerNouns = new LinearProbingHashST<String, Queue<Integer>>();
        nounsPerSynset = new RedBlackBST<Integer, Queue<String>>();
        synsetsPerNouns = new RedBlackBST<String, Queue<Integer>>();
        while (synsetText.hasNextLine()) {
            String[] currentSynArr = synsetText.readLine().split(",");
            int synId = Integer.parseInt(currentSynArr[0]);

            // Queue to store the nouns per synset
            Queue<String> thisSynsetNouns = new Queue<String>();

            // Parse the words of the current synset
            String[] synWordsArr = currentSynArr[1].split(" ");
            for (int i = 0; i < synWordsArr.length; i++) {
                thisSynsetNouns.enqueue(synWordsArr[i]);

                // Queue to store the synsets per noun
                Queue<Integer> oldQueue;
                if (synsetsPerNouns.contains(synWordsArr[i])) {
                    oldQueue = synsetsPerNouns.get(synWordsArr[i]);
                }
                else {
                    oldQueue = new Queue<Integer>();
                }
                oldQueue.enqueue(synId);
                synsetsPerNouns.put(synWordsArr[i], oldQueue);
            }
            nounsPerSynset.put(synId, thisSynsetNouns);
        }
    }

    // the set of all WordNet nouns
    public Iterable<String> nouns() {
        return synsetsPerNouns.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return synsetsPerNouns.contains(word);
    }

    // a synset that is a shortest common ancestor
    // of noun1 and noun2
    public String sca(String noun1, String noun2) {
        Iterable<Integer> noun1Subset = synsetsPerNouns.get(noun1);
        Iterable<Integer> noun2Subset = synsetsPerNouns.get(noun2);
        return nounsPerSynset.get(
                sca.ancestorSubset(noun1Subset, noun2Subset)).toString();
    }

    //
    // distance between noun1 and noun2 (defined below)
    public int distance(String noun1, String noun2) {
        Iterable<Integer> noun1Subset = synsetsPerNouns.get(noun1);
        Iterable<Integer> noun2Subset = synsetsPerNouns.get(noun2);
        return sca.lengthSubset(noun1Subset, noun2Subset);
    }

    // unit testing (required)
    public static void main(String[] args) {
        WordNet test = new WordNet(
                "synsets11.txt", "hypernyms11ManyPathsOneAncestor.txt");
        StdOut.println("Is a contained noun? : " + test.isNoun("a")); // True
        StdOut.println("Is a contained noun? : " + test.isNoun("z")); // False

        StdOut.println("All containted nouns: ");
        for (String a : test.nouns())
            StdOut.print(a + " ");

        StdOut.println("");
        StdOut.println("SCA between a and k: " + test.sca("a", "k")); // f
        StdOut.println("Distance between a and k: " + test.distance("a", "k")); // 4
    }

}
