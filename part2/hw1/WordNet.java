import edu.princeton.cs.algs4.DirectedDFS;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Topological;
import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.RedBlackBST;
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
        checkGraph();
        sap = new SAP(graph);
    }

    private void checkGraph() {
        // find root
        int root = 0;
        var visited = new SET<Integer>();
        visited.add(root);
        while (graph.adj(root).iterator().hasNext()) {
            for (var a : graph.adj(root)) {
                if (visited.contains(a)) {
                    // cycle!
                    throw new IllegalArgumentException();
                }
                root = a;
                break;
            }
        }
        var gr = graph.reverse();
        var dfs = new DirectedDFS(gr, root);
        for (var v = 0; v < gr.V(); v++) {
            if (!dfs.marked(v)) {
                // other roots!
                throw new IllegalArgumentException();
            }
        }

        var t = new Topological(graph);
        if (!t.hasOrder()) {
            // other roots!
            throw new IllegalArgumentException();
        }
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
        StdOut.print("Length between 12044 and 31625: ");
        StdOut.println(w.distance("Norrish", "contemplation"));
        StdOut.println(w.distance("contemplation", "Norrish"));
    }
}
