import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {
    private final ST<Integer, String> idToNoun;
    private final ST<String, Bag<Integer>> nounToID;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }
        idToNoun = new ST<>();
        nounToID = new ST<>();
        createSynsets(synsets);
        Digraph digraph = createHypernyms(hypernyms);

        DirectedCycle checkCycle = new DirectedCycle(digraph);
        if (checkCycle.hasCycle() || !isRootedDAG(digraph)) {
            throw new IllegalArgumentException();
        }
        sap = new SAP(digraph);
    }

    private void createSynsets(String synsets) {
        In synIn = new In(synsets);
        while (synIn.hasNextLine()) {
            String line = synIn.readLine();
            String[] segments = line.split(",");
            String nouns = segments[1];
            int idVal = Integer.parseInt(segments[0]);
            idToNoun.put(idVal, nouns); // add the group of nouns to the symboltable with corresponding id

            String[] nounArr = nouns.split(" ");

            for (int i = 0; i < nounArr.length; i++) {
                Bag<Integer> bag = nounToID.get(nounArr[i]); // find the bag of keys that correspond to each noun in the line
                if (bag == null) {
                    Bag<Integer> newBag = new Bag<>(); // create a new bag if none already
                    newBag.add(idVal); // add to bag
                    nounToID.put(nounArr[i], newBag);
                } else {
                    bag.add(idVal); // add to bag
                }
            }
        }
    }

    private Digraph createHypernyms(String hypernyms) {
        Digraph digraph = new Digraph(idToNoun.size());

        In hyperIn = new In(hypernyms);
        while (hyperIn.hasNextLine()) {
            String line = hyperIn.readLine();
            String[] individual = line.split(",");
            int idVal = Integer.parseInt(individual[0]);
            for (int i = 1; i < individual.length; i++) {
                digraph.addEdge(idVal, Integer.parseInt(individual[i])); // add edges between the ID and noun keys
            }
        }
        return digraph;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounToID.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return nounToID.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException();
        }
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        return sap.length(nounToID.get(nounA), nounToID.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException();
        }
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        int idAncestor = sap.ancestor(nounToID.get(nounA), nounToID.get(nounB));
        return idToNoun.get(idAncestor);
    }

    // check if there is no Cycle
    private boolean isRootedDAG(Digraph d) {
        int roots = 0;
        for (int i = 0; i < d.V(); i++) {
            if (!d.adj(i).iterator().hasNext()) {
                roots++;
                if (roots > 1) {
                    return false;
                }
            }
        }
        return roots == 1;
    }


    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet(args[0], args[1]);
        for (String s : wn.nouns()) {
            StdOut.println(s);
        }
        while (!StdIn.isEmpty()) {
            String nounA = StdIn.readLine();
            String nounB = StdIn.readLine();
            int distance = wn.distance(nounA, nounB);
            String ancestor = wn.sap(nounA, nounB);
            StdOut.println("length = " + distance);
            StdOut.println("ancestor = " + ancestor);
        }
    }

}
