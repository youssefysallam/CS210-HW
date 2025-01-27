import dsa.DiGraph;
import dsa.LinkedQueue;
import dsa.SeparateChainingHashST;
import stdlib.In;
import stdlib.StdIn;
import stdlib.StdOut;

public class ShortestCommonAncestor {
    private final DiGraph G;

    // Constructs a ShortestCommonAncestor object given a rooted DAG.
    public ShortestCommonAncestor(DiGraph G) {
        // corner case
        if (G == null) {
            throw new NullPointerException("G is null");
        }
        this.G = G;
    }

    // Returns length of the shortest ancestral path between vertices v and w.
    public int length(int v, int w) {
        // corner case
        if ( v < 0 || v >= G.V()) {
            throw new IndexOutOfBoundsException("v is invalid");
        }
        if (w < 0 || w >= G.V()) {
            throw new IndexOutOfBoundsException("w is invalid");
        }


        int ancestor = ancestor(v, w);
        if (ancestor == -1) {
            return  -1;
        }
        return distFrom(v).get(ancestor) + distFrom(w).get(ancestor);
    }

    // Returns a shortest common ancestor of vertices v and w.
    public int ancestor(int v, int w) {
        // corner case
        if ( v < 0 || v >= G.V()) {
            throw new IndexOutOfBoundsException("v is invalid");
        }
        if (w < 0 || w >= G.V()) {
            throw new IndexOutOfBoundsException("w is invalid");
        }

        SeparateChainingHashST<Integer, Integer> distV = distFrom(v);
        SeparateChainingHashST<Integer, Integer> distW = distFrom(w);

        int minDistance = Integer.MAX_VALUE;
        int sca = -1;

        for (int commonAncestor : distV.keys()) {
            if (distW.contains(commonAncestor)) {
                int distance = distV.get(commonAncestor) +distW.get(commonAncestor);
                if (distance < minDistance) {
                    minDistance = distance;
                    sca = commonAncestor;
                }
            }
        }
        return sca;
    }

    // Returns length of the shortest ancestral path of vertex subsets A and B.
    public int length(Iterable<Integer> A, Iterable<Integer> B) {
        // corner case
        if (A == null) {
            throw new NullPointerException("A is null");
        }
        if (B == null) {
            throw new NullPointerException("B is null");
        }
        if (!A.iterator().hasNext()) {
            throw new IllegalArgumentException("A is empty");
        }
        if (!B.iterator().hasNext()) {
            throw new IllegalArgumentException("B is empty");
        }


        int[] triad = triad(A, B);
        return triad[0];
    }

    // Returns a shortest common ancestor of vertex subsets A and B.
    public int ancestor(Iterable<Integer> A, Iterable<Integer> B) {
        // corner case
        if (A == null) {
            throw new NullPointerException("A is null");
        }
        if (B == null) {
            throw new NullPointerException("B is null");
        }
        if (!A.iterator().hasNext()) {
            throw new IllegalArgumentException("A is empty");
        }
        if (!B.iterator().hasNext()) {
            throw new IllegalArgumentException("B is empty");
        }


        int[] triad= triad(A, B);
        return triad[1];
    }

    // Returns a map of vertices reachable from v and their respective shortest distances from v.
    private SeparateChainingHashST<Integer, Integer> distFrom(int v) {
        SeparateChainingHashST<Integer, Integer> dist = new SeparateChainingHashST<>();
        LinkedQueue<Integer> q = new LinkedQueue<>();

        dist.put(v, 0);
        q.enqueue(v);

        while (!q.isEmpty()) {
            int current = q.dequeue();
            for (int w : G.adj(current)) {
                if (!dist.contains(w)) {
                    dist.put(w, dist.get(current) + 1);
                    q.enqueue(w);
                }
            }
        }
        return dist;
    }

    // Returns an array consisting of a shortest common ancestor a of vertex subsets A and B,
    // vertex v from A, and vertex w from B such that the path v-a-w is the shortest ancestral
    // path of A and B.
    private int[] triad(Iterable<Integer> A, Iterable<Integer> B) {
        int shortestDistance = Integer.MAX_VALUE;
        int bestAncestor = -1;
        int vBest = -1;
        int wBest = -1;

        for (int v : A) {
            for (int w : B) {
                int currentAncestor = ancestor(v, w);
                int distanceV = distFrom(v).get(currentAncestor);
                int distanceW = distFrom(w).get(currentAncestor);
                int currentDistance = distanceV + distanceW;

                if (currentDistance < shortestDistance) {
                    shortestDistance = currentDistance;
                    bestAncestor = currentAncestor;
                    vBest = v;
                    wBest = w;
                }
            }
        }
        return new int[]{shortestDistance, bestAncestor, vBest};
    }


    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        DiGraph G = new DiGraph(in);
        in.close();
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}