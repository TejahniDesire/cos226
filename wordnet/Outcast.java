import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int championLength = 0;
        String championString = "";
        for (String noun : nouns) {
            int dTotal = 0;
            for (String noun2 : nouns) {
                if (!noun2.equals(noun))
                    dTotal = dTotal + wordNet.distance(noun, noun2);
            }
            if (dTotal > championLength) {
                championLength = dTotal;
                championString = noun;
            }
        }
        return championString;
    }

    // test client (see below)
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }

}
