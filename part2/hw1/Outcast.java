import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;
import java.util.ArrayList;

public class Outcast {
    public Outcast(WordNet _wordnet) {
        wordnet = _wordnet;
    }

    private WordNet wordnet;

    public String outcast(String[] nouns) {
        var outcast = nouns[0];
        int maxDistance = -1;
        for (var s : nouns) {
            int distance = 0;
            for (var o : nouns) {
                var d = wordnet.distance(s, o);
                distance += d;

                //StdOut.print("Distance from ");
                //StdOut.print(s);
                //StdOut.print(" to ");
                //StdOut.print(o);
                //StdOut.print(": ");
                //StdOut.println(d);
            }
            if (distance > maxDistance) {
                maxDistance = distance;
                outcast = s;
            }
        }
        return outcast;
    }

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
