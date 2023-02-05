import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.Topological;
import java.util.Arrays;

public class WordNet {
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }

        allWords = new SET<String>();
        words = new RedBlackBST<Integer, SET<String>>();
        numWords = new RedBlackBST<String, SET<Integer>>();
        var in = new In(synsets);
        while (!in.isEmpty()) {
            var s = in.readLine();
            var strings = s.split(",");
            var key = Integer.parseInt(strings[0]);
            var syns = strings[1].split(" ");
            var setS = new SET<String>();
            words.put(key, setS);
            for (var syn : syns) {
                setS.add(syn);
                allWords.add(syn);
                var set = numWords.get(syn);
                if (set == null) {
                    set = new SET<Integer>();
                    numWords.put(syn, set);
                }
                set.add(key);
            }
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
        var t = new Topological(graph);
        if (!t.hasOrder()) {
            throw new IllegalArgumentException();
        }
        sap = new SAP(graph);
    }

    private RedBlackBST<Integer, SET<String>> words;
    private RedBlackBST<String, SET<Integer>> numWords;
    private SET<String> allWords;
    private Digraph graph;
    private SAP sap;

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return numWords.keys();
    }

    private SET<Integer> findWord(String word) {
        return numWords.get(word);
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return allWords.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException();
        }

        var w1 = -1;
        var w2 = -1;
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }

        return sap.length(findWord(nounA), findWord(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException();
        }
        var ca = sap.ancestor(findWord(nounA), findWord(nounB));
        if (ca == -1) {
            return "";
        }
        return String.join(" ", words.get(ca));
    }

    public static void main(String[] args) {
        var w = new WordNet("synsets.txt", "hypernyms.txt");
        StdOut.print("Nguni is noun: ");
        StdOut.println(w.isNoun("Nguni"));
        StdOut.print("Qadcdk is noun: ");
        StdOut.println(w.isNoun("qadcdk"));
    }
}
