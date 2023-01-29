import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.LinearProbingHashST;
import java.util.Arrays;
import java.util.List;

public class WordNet {
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }

        var in = new In(synsets);
        words = new LinearProbingHashST<Integer, String>();
        while (!in.isEmpty()) {
            var s = in.readLine();
            var strings = s.split(",");
            var key = Integer.parseInt(strings[0]);
            words.put(key, strings[1]);
        }

        graph = new Digraph(words.size());
        in = new In(hypernyms);
        while (!in.isEmpty()) {
            var s = in.readString();
            var strings = s.split(",");
            var key = Integer.parseInt(strings[0]);
            for (int i = 1; i < strings.length; i++) {
                var hypernym = Integer.parseInt(strings[i]);
                graph.addEdge(key, hypernym);
            }
        }
    }

    private LinearProbingHashST<Integer, String> words;
    private Digraph graph;

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        var ret = new String[words.size()];
        int i = 0;
        for (var k : words.keys()) {
            ret[i++] = words.get(k);
        }
        return Arrays.asList(ret);
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }

        for (var k : words.keys()) {
            if (word.equals(words.get(k))) {
                return true;
            }
        }

        return false;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException();
        }

        return 0;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException();
        }

        return "a";
    }

    // do unit testing of this class
    public static void main(String[] args) {
        var w = new WordNet("synsets.txt", "hypernyms.txt");
        StdOut.print("Nguni is noun: ");
        StdOut.println(w.isNoun("Nguni"));
        StdOut.print("Qadcdk is noun: ");
        StdOut.println(w.isNoun("qadcdk"));
    }
}
