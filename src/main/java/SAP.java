import java.util.Arrays;

public class SAP {
    private static final int INFINITY = Integer.MAX_VALUE;
    private static final int UNKNOWN = -1;

    private Digraph digraph;

    public SAP(Digraph digraph) {
        this.digraph = clone(digraph);
    }

    private Digraph clone(Digraph digraphToClone) {
        Digraph clone = new Digraph(digraphToClone.V());
        for (int v = 0; v < clone.V(); v++) {
            Iterable<Integer> vertices = digraphToClone.adj(v);
            for (int w : vertices) {
                clone.addEdge(v, w);
            }
        }
        return clone;
    }

    public int length(int v, int w) {
        return length(Arrays.asList(v), Arrays.asList(w));
    }

    public int ancestor(int v, int w) {
        return ancestor(Arrays.asList(v), Arrays.asList(w));
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return findSAP(v, w)[1];
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return findSAP(v, w)[0];
    }

    private int[] findSAP(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph,
                v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph,
                w);

        int[] sapAncLength = { INFINITY, INFINITY };

        for (int i = 0; i < digraph.V(); i++) {
            int distFromIToV = bfsV.distTo(i);
            int distFromIToW = bfsW.distTo(i);

            if (distFromIToV != INFINITY && distFromIToW != INFINITY) {
                int ancPathLength = distFromIToV + distFromIToW;
                if (sapAncLength[1] == INFINITY
                        || ancPathLength < sapAncLength[1]) {
                    sapAncLength[0] = i;
                    sapAncLength[1] = ancPathLength;
                }

            }
        }

        return convertToUnknownIfInfinity(sapAncLength);
    }

    private int[] convertToUnknownIfInfinity(int[] sapAncLength) {
        if (sapAncLength[0] == INFINITY) {
            int[] unknown = { UNKNOWN, UNKNOWN };
            return unknown;
        } else {
            return sapAncLength;
        }
    }

}
