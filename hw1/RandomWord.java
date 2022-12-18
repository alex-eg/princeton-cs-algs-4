import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        String win = "";
        int n = 1;
        while (!StdIn.isEmpty()) {
            var s = StdIn.readString();
            if (StdRandom.bernoulli(1.0 / n)) {
                win = s;
            }
            n++;
        }

        StdOut.println(win);
    }
}
