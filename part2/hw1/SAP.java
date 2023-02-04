import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

public class SAP {
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException();
        }
        graph = G;
    }

    private Digraph graph;

    private class LockstepBFS {

    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v > graph.V() - 1 || w > graph.V() - 1) {
            throw new IllegalArgumentException();
        }

        var a = ancestor(v, w);
        if (a == -1) {
            return -1;
        }

        //StdOut.print("Ancestor of ");
        //StdOut.print(v);
        //StdOut.print(" and ");
        //StdOut.print(w);
        //StdOut.print(" is ");
        //StdOut.println(a);

        var p = new BreadthFirstDirectedPaths(graph.reverse(), a);
        int len = 0;
        for (var q : p.pathTo(v)) {
            len++;
        }
        for (var q : p.pathTo(w)) {
            len++;
        }

        return len - 2;
    }

    private int root(int v) {
        var i = graph.adj(v);
        var prev = v;
        while (i != null ) {
            if (i.iterator().hasNext()) {
                prev = i.iterator().next();
            } else {
                break;
            }
            i = graph.adj(prev);
        }
        return prev;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v > graph.V() - 1 || w > graph.V() - 1) {
            throw new IllegalArgumentException();
        }

        if (v == w) {
            return v;
        }

        var rv = root(v);
        if (rv != root(w)) {
            // StdOut.print("Roots of ");
            // StdOut.print(v);
            // StdOut.print(" and ");
            // StdOut.print(w);
            // StdOut.println(" differ!");
            return -1;
        }

        var d = new BreadthFirstDirectedPaths(graph.reverse(), rv);
        var pathV = d.pathTo(v);
        if (pathV == null) {
            // StdOut.println("Path to v is null");
            return -1;
        }

        var pathW = d.pathTo(w);
        if (pathW == null) {
            // StdOut.println("Path to w is null");
            return -1;
        }

        var iv = pathV.iterator();
        var iw = pathW.iterator();

        /*
        StdOut.print("Path from ");
        StdOut.print(rv);
        StdOut.print(" to ");
        StdOut.print(v);
        StdOut.print(": ");
        while (iv.hasNext()) {
            StdOut.print(iv.next());
            StdOut.print("-");
        }
        StdOut.println("");

        StdOut.print("Path from ");
        StdOut.print(rv);
        StdOut.print(" to ");
        StdOut.print(w);
        StdOut.print(": ");
        while (iw.hasNext()) {
            StdOut.print(iw.next());
            StdOut.print("-");
        }
        StdOut.println("");
        //*/

        var p = -1;
        iv = pathV.iterator();
        iw = pathW.iterator();
        while (iv.hasNext() && iw.hasNext()) {
            var n1 = iv.next();
            var n2 = iw.next();
            if (n1.equals(n2) == false) {
                //StdOut.print("n1 = ");
                //StdOut.print(n1);
                //StdOut.print(" n2 = ");
                //StdOut.print(n2);
                //StdOut.print(" n1.equals(n2)? ");
                //StdOut.print(n1.equals(n2));
                //StdOut.print(" n2.equals(n1)? ");
                //StdOut.print(n2.equals(n1));
                //StdOut.print(" shortest common ancestor: ");
                //StdOut.println(p);
                return p;
            } else {
                //StdOut.print("n1 == n2 == ");
                //StdOut.println(n1);
                p = n1;
            }
        }
        return p;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }

        int len = Integer.MAX_VALUE;
        for (var i : w) {
            if (i == null || i > graph.V() - 1) {
                throw new IllegalArgumentException();
            }

            for (var j : v) {
                if (j == null || j > graph.V() - 1) {
                    throw new IllegalArgumentException();
                }

                var l = length(i, j);
                if (l != -1 && l < len) {
                    len = l;
                }
            }
        }

        if (len != Integer.MAX_VALUE) {
            return len;
        } else {
            return -1;
        }
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }

        int anc = -1;
        int len = Integer.MAX_VALUE;
        for (var i : w) {
            if (i == null || i > graph.V() - 1) {
                throw new IllegalArgumentException();
            }

            for (var j : v) {
                if (j == null || j > graph.V() - 1) {
                    throw new IllegalArgumentException();
                }

                var l = length(i, j);
                if (l != -1 && l < len) {
                    len = l;
                    anc = ancestor(i, j);
                }
            }
        }

        return anc;
    }

    public static void main(String[] args) {
        Digraph g = new Digraph(10);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 3);
        g.addEdge(2, 4);
        g.addEdge(2, 5);
        g.addEdge(5, 6);
        g.addEdge(0, 7);

        var s = new SAP(g);
        StdOut.print("3 5: ");
        StdOut.print(s.ancestor(3, 5));
        StdOut.print(" ");
        StdOut.println(s.length(3, 5));

        StdOut.print("5 3: ");
        StdOut.print(s.ancestor(5, 3));
        StdOut.print(" ");
        StdOut.println(s.length(5, 3));

        StdOut.print("4 6: ");
        StdOut.print(s.ancestor(4, 6));
        StdOut.print(" ");
        StdOut.println(s.length(4, 6));

        StdOut.print("6 7: ");
        StdOut.print(s.ancestor(6, 7));
        StdOut.print(" ");
        StdOut.println(s.length(6, 7));

        StdOut.print("1 2: ");
        StdOut.print(s.ancestor(1, 2));
        StdOut.print(" ");
        StdOut.println(s.length(1, 2));

        StdOut.print("6 8: ");
        StdOut.print(s.ancestor(6, 8));
        StdOut.print(" ");
        StdOut.println(s.length(6, 8));
    }
}
