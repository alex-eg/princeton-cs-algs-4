import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.RedBlackBST;
import java.util.Arrays;

public class SAP {
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException();
        }
        graph = new Digraph(G);
    }

    private Digraph graph;

    private static class LockstepSAP {
        public LockstepSAP(Digraph g, Iterable<Integer> v, Iterable<Integer> w) {
            // dots = new RedBlackBST<Integer, Integer>();
            var graph = g;
            sap = -1;
            length = Integer.MAX_VALUE;

            // next search iteration
            var qv = new Queue<Integer>();
            var qw = new Queue<Integer>();

            // visited verts
            var sv = new RedBlackBST<Integer, Integer>();
            var sw = new RedBlackBST<Integer, Integer>();

            for (var iv : v) {
                sv.put(iv, 0);
                qv.enqueue(iv);
            }

            for (var iw : w) {
                // early check
                if (sv.contains(iw)) {
                    sap = iw;
                    length = 0;
                    return;
                }

                sw.put(iw, 0);
                qw.enqueue(iw);
            }

            //StdOut.println("-----Start ---------");
            //StdOut.print("v: ");
            //StdOut.print(v);
            //StdOut.print(" adj(v): [");
            //for (var a : graph.adj(v.iterator().next())) {
            //    StdOut.print(a);
            //    StdOut.print(", ");
            //}
            //StdOut.print("] w: ");
            //StdOut.print(w);
            //StdOut.print(" adj(w): [");
            //for (var a : graph.adj(w.iterator().next())) {
            //    StdOut.print(a);
            //    StdOut.print(", ");
            //}
            //StdOut.println("]");
            //StdOut.print("qv: ");
            //StdOut.println(qv);
            //StdOut.print("qw: ");
            //StdOut.println(qw);
            //StdOut.println();
            //StdOut.println();

            int lv = 0;
            int lw = 0;
            while (!qv.isEmpty() || !qw.isEmpty()) {
                //StdOut.println("--------------------");
                //StdOut.print("qv: ");
                //StdOut.println(qv);
                //StdOut.print("qw: ");
                //StdOut.println(qw);

                if (!qv.isEmpty()) {
                    lv++;
                }

                if (!qw.isEmpty()) {
                    lw++;
                }

                visitQueue(g, qv, lv, sv, sw);
                visitQueue(g, qw, lw, sw, sv);
            }

            if (sap == -1) {
                length = -1;
            }

            //StdOut.print("sv: ");
            //for (var k : sv.keys()) {
            //    StdOut.print(k);
            //    StdOut.print(" - ");
            //    StdOut.println(sv.get(k));
            //}
            //
            //StdOut.print("sw: ");
            //for (var k : sw.keys()) {
            //    StdOut.print(k);
            //    StdOut.print(" - ");
            //    StdOut.println(sw.get(k));
            //}
        }

        // private RedBlackBST<Integer, Integer> dots;
        // private int dot = 0;

        private void visitQueue(Digraph g, Queue<Integer> q, int currentLength, RedBlackBST<Integer, Integer> visited, RedBlackBST<Integer, Integer> otherVisited) {
            // StdOut.println("------------------------------");
            // StdOut.print("Distance = ");
            // StdOut.println(currentLength);
            var size = q.size();
            //StdOut.println("------------------------------");
            for (int i = 0; i < size; i++) {
                var v = q.dequeue();
                for (var a : g.adj(v)) {
                    if (otherVisited.contains(a)) {
                        // StdOut.println("Found match!");
                        // StdOut.print("Distances: ");
                        var otherL = otherVisited.get(a);
                        var l = currentLength + otherL;
                        // StdOut.print(currentLength);
                        // StdOut.print(" ");
                        // StdOut.println(otherL);
                        if (l < length) {
                            sap = a;
                            length = l;
                            if (otherL == 0) {
                                // the shortest possible one
                                return;
                            }
                        }
                    }

                    // if (!dots.contains(a)) {
                    //     dots.put(a, dot++);
                    // }

                    //StdOut.println(dots.get(a));
                    if (!visited.contains(a)) {
                        visited.put(a, currentLength);
                        q.enqueue(a);
                    }
                }

                // StdOut.print("Visiting ");
                // StdOut.println(v);

                // if (!dots.contains(v)) {
                //     dots.put(v, dot++);
                // }
                //StdOut.print("V: ");
                //StdOut.println(dots.get(v));
            }
        }

        public int sap;
        public int length;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v > graph.V() - 1 || w > graph.V() - 1 || w < 0 || v < 0) {
            throw new IllegalArgumentException();
        }

        if (v == w) {
            return 0;
        }

        //StdOut.print("Ancestor of ");
        //StdOut.print(v);
        //StdOut.print(" and ");
        //StdOut.print(w);
        //StdOut.print(" is ");
        //StdOut.println(a);

        var p = new LockstepSAP(graph, Arrays.asList(v), Arrays.asList(w));
        return p.length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v > graph.V() - 1 || w > graph.V() - 1 || v < 0 || w < 0) {
            throw new IllegalArgumentException();
        }

        if (v == w) {
            return v;
        }

        var p = new LockstepSAP(graph, Arrays.asList(v), Arrays.asList(w));
        return p.sap;
    }

    private void checkIterable(Iterable<Integer> v) {
        if (v == null) {
            throw new IllegalArgumentException();
        }

        for (var i : v) {
            if (i == null || i > graph.V() - 1) {
                throw new IllegalArgumentException();
            }
        }
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        checkIterable(v);
        checkIterable(w);

        var p = new LockstepSAP(graph, v, w);
        return p.length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        checkIterable(v);
        checkIterable(w);

        var p = new LockstepSAP(graph, v, w);
        return p.sap;
    }

    public static void main(String[] args) {
        //Digraph g = new Digraph(6);
        //g.addEdge(1, 0);
        //g.addEdge(1, 2);
        //g.addEdge(2, 3);
        //g.addEdge(3, 4);
        //g.addEdge(4, 5);
        //g.addEdge(5, 0);

        //Digraph g = new Digraph(10);
        //g.addEdge(1, 2);
        //g.addEdge(1, 7);
        //g.addEdge(2, 3);
        //g.addEdge(3, 4);
        //g.addEdge(4, 5);
        //g.addEdge(5, 6);
        //g.addEdge(7, 8);
        //g.addEdge(9, 0);
        //g.addEdge(8, 6);
        //g.addEdge(0, 8);
        //
        //var s = new SAP(g);
        //StdOut.println("9 4: ");
        //StdOut.println(s.ancestor(9, 4));
        //StdOut.println(s.length(9, 4));
        //StdOut.println("1 4: ");
        //StdOut.println(s.ancestor(1, 4));
        //StdOut.println(s.length(1, 4));
        //
        //g = new Digraph(10);
        //g.addEdge(1, 0);
        //g.addEdge(2, 1);
        //g.addEdge(3, 2);
        //g.addEdge(4, 2);
        //g.addEdge(5, 2);
        //g.addEdge(6, 5);
        //g.addEdge(7, 0);
        //
        //s = new SAP(g);
        //StdOut.print("3 5: ");
        //StdOut.print(s.ancestor(3, 5));
        //StdOut.print(" ");
        //StdOut.println(s.length(3, 5));
        //
        //StdOut.print("5 3: ");
        //StdOut.print(s.ancestor(5, 3));
        //StdOut.print(" ");
        //StdOut.println(s.length(5, 3));
        //
        //StdOut.print("4 6: ");
        //StdOut.print(s.ancestor(4, 6));
        //StdOut.print(" ");
        //StdOut.println(s.length(4, 6));
        //
        //StdOut.print("6 7: ");
        //StdOut.print(s.ancestor(6, 7));
        //StdOut.print(" ");
        //StdOut.println(s.length(6, 7));
        //
        //StdOut.print("1 2: ");
        //StdOut.print(s.ancestor(1, 2));
        //StdOut.print(" ");
        //StdOut.println(s.length(1, 2));
        //
        //StdOut.print("6 8: ");
        //StdOut.print(s.ancestor(6, 8));
        //StdOut.print(" ");
        //StdOut.println(s.length(6, 8));

        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        var s = new SAP(G);
        StdOut.println(s.length(13, 8));
        StdOut.println(s.length(8, 13));
        StdOut.println(s.ancestor(10, 7));
        StdOut.println("------------------------------");
        StdOut.println(s.length(7, 10));
        StdOut.println(s.ancestor(7, 10));
        //StdOut.println(s.length(70860, 18158));
        //StdOut.println(s.length(18158, 70860));
    }
}
