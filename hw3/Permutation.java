import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        if (k == 0) {
            return;
        }
        var q = new RandomizedQueue<String>();

        int n = 0;
        while (!StdIn.isEmpty()) {
            var s = StdIn.readString();
            n++;
            if (q.size() == k) {
                if (StdRandom.bernoulli((double)k / n)) {
                    q.dequeue();
                    q.enqueue(s);
                }
            } else {
                q.enqueue(s);
            }
        }

        for (var s : q) {
            StdOut.println(s);
        }
    }
}
