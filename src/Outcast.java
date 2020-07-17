import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordnet;
    private String outcast;

    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
        this.outcast = null;

    }       // constructor takes a WordNet object

    public String outcast(String[] nouns) {
        int max = 0;
        for (String currentNoun : nouns) {
            int total = 0;
            for (String nextNoun : nouns) {
                int distance = wordnet.distance(currentNoun, nextNoun);
                total += distance;
            }
            if (total > max) {
                max = total;
                outcast = currentNoun;
            }
        }
        return outcast;
    }  // given an array of WordNet nouns, return an outcast

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
