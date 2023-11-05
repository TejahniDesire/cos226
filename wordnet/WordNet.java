import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {

    private Digraph digraph; // underlying digraph for WordNet

    // Stores synsets and corresponding nouns
    // Stores Nouns and corresponding synset
    private LinearProbingHashST<String, Queue<Integer>> synsetNouns;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        In synsetsText = new In(synsets);
        In hypernymText = new In(hypernyms);
        hypernymParser(hypernymText);
        synsetParser(synsetsText);
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
        synsetNouns = new LinearProbingHashST<String, Queue<Integer>>();
        while (synsetText.hasNextLine()) {
            String[] currentSynArr = synsetText.readLine().split(",");
            int synId = Integer.parseInt(currentSynArr[0]);

            // Parse the words of the current synset
            String[] synWordsArr = currentSynArr[1].split(" ");
            for (int i = 0; i < synWordsArr.length; i++) {
                Queue<Integer> oldQueue;
                if (synsetNouns.contains(synWordsArr[i])) {
                    oldQueue = synsetNouns.get(synWordsArr[i]);
                }
                else {
                    oldQueue = new Queue<Integer>();
                }
                oldQueue.enqueue(synId);
                synsetNouns.put(synWordsArr[i], oldQueue);
            }
        }
    }

    // the set of all WordNet nouns
    public Iterable<String> nouns() {
        return synsetNouns.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return synsetNouns.contains(word);
    }

    // // a synset (second field of synsets.txt) that is a shortest common ancestor
    // // of noun1 and noun2 (defined below)
    // public String sca(String noun1, String noun2) {
    //
    // }
    //
    // // distance between noun1 and noun2 (defined below)
    // public int distance(String noun1, String noun2) {
    //
    // }

    // unit testing (required)
    public static void main(String[] args) {
        WordNet test = new WordNet("synsets11.txt", "hypernyms11ManyPathsOneAncestor.txt");
        StdOut.println(test.isNoun("a"));
        for (String a : test.nouns())
            StdOut.println(a);

    }

}
