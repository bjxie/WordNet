import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph digraph;
    private BreadthFirstDirectedPaths[] bfs;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        digraph = new Digraph(G);
        bfs = new BreadthFirstDirectedPaths[digraph.V()]; // creates a new bfs array with size of number of vertices
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || w < 0 ||
                v > digraph.V() - 1 || w > digraph.V() - 1) {
            throw new IllegalArgumentException();
        }
        if (bfs[v] == null) {
            bfs[v] = new BreadthFirstDirectedPaths(digraph, v); // all bfs[] indices are initially initialized to null
            // create a new bfs at the input vertex v
        }
        if (bfs[w] == null) {
            bfs[w] = new BreadthFirstDirectedPaths(digraph, w); // create a new bfs at input vertex w
        }
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < digraph.V(); i++) {
            if (bfs[v].hasPathTo(i) && (bfs[w].hasPathTo(i))) { // for every vertex i, check if vertex v and w have path to i
                int length = bfs[v].distTo(i) + bfs[w].distTo(i);
                if (length < min) {
                    min = length; // update minimum length
                }
            }
        }
        bfs[v] = null; // reset bfs indices to save space
        bfs[w] = null;

        if (min == Integer.MAX_VALUE) {
            return -1; // if no path, return -1
        } else {
            return min;
        }
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    // same code as length() but returns closest vertex, not minimum length
    public int ancestor(int v, int w) {
        int shortestAncestor = -1;
        if (v < 0 || w < 0 ||
                v > digraph.V() - 1 || w > digraph.V() - 1) {
            throw new IllegalArgumentException();
        }
        if (bfs[v] == null) {
            bfs[v] = new BreadthFirstDirectedPaths(digraph, v);
        }
        if (bfs[w] == null) {
            bfs[w] = new BreadthFirstDirectedPaths(digraph, w);
        }
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < digraph.V(); i++) {
            if (bfs[v].hasPathTo(i) && (bfs[w].hasPathTo(i))) {
                int length = bfs[v].distTo(i) + bfs[w].distTo(i);
                if (length < min) {
                    shortestAncestor = i; // keep track of which vertex is the shortest ancestor
                    min = length;
                }
            }
        }
        bfs[v] = null;
        bfs[w] = null;

        if (min == Integer.MAX_VALUE) {
            return -1;
        } else {
            return shortestAncestor;
        }
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    // similar code but check each vertex in the iterable
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        int min = Integer.MAX_VALUE;
        for (Integer thisVertex : v) {
            if (thisVertex == null || thisVertex < 0 || thisVertex > digraph.V() - 1) {
                throw new IllegalArgumentException();
            }
            for (Integer thatVertex : w) {
                if (thatVertex == null || thatVertex < 0 || thatVertex > digraph.V() - 1) {
                    throw new IllegalArgumentException();
                }
                int length = length(thisVertex, thatVertex);
                if (length != -1 && length < min) {
                    min = length;
                }
            }
        }
        if (min == Integer.MAX_VALUE) {
            return -1;
        }
        return min;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        int shortestAncestorIterable = -1;
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        int min = Integer.MAX_VALUE;
        for (Integer thisVertex : v) {
            if (thisVertex == null || thisVertex < 0 || thisVertex > digraph.V() - 1) {
                throw new IllegalArgumentException();
            }
            for (Integer thatVertex : w) {
                if (thatVertex == null || thatVertex < 0 || thatVertex > digraph.V() - 1) {
                    throw new IllegalArgumentException();
                }
                int length = length(thisVertex, thatVertex);
                if (length != -1 && length < min) {
                    min = length;
                    shortestAncestorIterable = ancestor(thisVertex, thatVertex);
                }
            }
        }
        if (min == Integer.MAX_VALUE) {
            return -1;
        }
        return shortestAncestorIterable;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}

