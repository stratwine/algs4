/*************************************************************************
 *  Compilation:  javac BreadthFirstPaths.java
 *  Execution:    java BreadthFirstPaths G s
 *  Dependencies: Graph.java Queue.java Stack.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/41undirected/tinyCG.txt
 *
 *  Run breadth first search on an undirected graph.
 *  Runs in O(E + V) time.
 *
 *  %  java Graph tinyCG.txt
 *  6 8
 *  0: 2 1 5 
 *  1: 0 2 
 *  2: 0 1 3 4 
 *  3: 5 4 2 
 *  4: 3 2 
 *  5: 3 0 
 *
 *  %  java BreadthFirstPaths tinyCG.txt 0
 *  0 to 0 (0):  0
 *  0 to 1 (1):  0-1
 *  0 to 2 (1):  0-2
 *  0 to 3 (2):  0-2-3
 *  0 to 4 (2):  0-2-4
 *  0 to 5 (1):  0-5
 *
 *************************************************************************/

/**
 * There are a lot of fields which we feel would be useful, if it were present in the Graph class
 * eg: marked   | was this vertex already visited, is it reachable from the BFS(v)
 * 
 * But well, we do not have such a boolean flag in each vertex
 * Remember: We do not have a vertex as a 'Vertex' class but as a mere number
 * 
 * So this will have to be stored in a separate array.
 * We create such arrays in this class
 * 
 * edgeTo | how did I reach this vertex ? From which vertex did I reach here ?
 * distTo | what's the sortest distance (while doing a bfs(v)) to this vertex ?
 * 
 * Constructor:
 *   Take a Graph and a BFS source vertex
 *   Create marked[],edgeTo[],distTo[] arrays all of size G.V()
 *   And invoke away the bfs call. Why wait :)
 */

public class BreadthFirstPaths {
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] marked;  // marked[v] = is there an s-v path
    private int[] edgeTo;      // edgeTo[v] = previous edge on shortest s-v path
    private int[] distTo;      // distTo[v] = number of edges shortest s-v path

    // single source
    public BreadthFirstPaths(Graph G, int s) {
        marked = new boolean[G.V()]; //Get the number of vertices. Create a marked[] with that size
        distTo = new int[G.V()]; 
        edgeTo = new int[G.V()];
        bfs(G, s);

        assert check(G, s);
    }

    // multiple sources
    public BreadthFirstPaths(Graph G, Iterable<Integer> sources) {
        marked = new boolean[G.V()];
        distTo = new int[G.V()];
        edgeTo = new int[G.V()];
        for (int v = 0; v < G.V(); v++) distTo[v] = INFINITY;
        bfs(G, sources);
    }


    // BFS from single source
    
    /**
     *  On each adjacent vertex, 
     *     update the marked[w],edgeTo[w],distTo[w] and then add to queue
     *     How do I trace back the path at the end ?
     *     Take the edgesTo[w] and go through it till sourceVertex s. That gives the path taken
     */
    
    
    
    /*
     * BFS
     * Initialization Part:
     *   All distances are initialized to INFINITY
     *   Un-explored | non-reachable vertices stay with INFINITY at the end
     *   Distance for s to s is 0
     *   Source is definitely visited. So that's marked
     *   We definitely need a Queue. So create a Queue
     *   Start with the source added the queue
     *   
     * Traversal Part:
     *   As long as the queue is not empty
     *   Dequeue
     *   For each dequed vertex, we find the adjacent vertices and add them to the queue
     *   But wait ! Before adding to the queue, there are few fields to be updated
     *     Update distTo[w] as dist[v]+1
     *     Update edgeTo[w] with v
     *     Update marked[w] with true
     *   
     *   This continues until all reachable vertices are visited, level by level
     *   Useful: In maze, to find the reachability (connected or not)
     * 
     * Finally: Check through marked to see if a vertex is reachable or not
     *          Traverse backwards from edgeTo to find the path taken
     *          distTo gives number of nodes between the source and that vertex
     * 
     * For multiple sources:
     *   Do the same as above for a Collection of sources
     *   But : Skip the vertices already marked 
     *         (Don't add them to the queue)
     *  
     */
    
    private void bfs(Graph G, int s) {
        Queue<Integer> q = new Queue<Integer>();
        for (int v = 0; v < G.V(); v++) distTo[v] = INFINITY; 
        distTo[s] = 0; 
        marked[s] = true;
        q.enqueue(s);

        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) { 
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    q.enqueue(w);
                }
            }
        }
    }

    // BFS from multiple sources
    private void bfs(Graph G, Iterable<Integer> sources) {
        Queue<Integer> q = new Queue<Integer>();
        for (int s : sources) {
            marked[s] = true;
            distTo[s] = 0;
            q.enqueue(s);
        }
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    q.enqueue(w);
                }
            }
        }
    }

    // is there a path between s (or sources) and v?
    public boolean hasPathTo(int v) {
        return marked[v];
    }

    // length of shortest path between s (or sources) and v
    public int distTo(int v) {
        return distTo[v];
    }

    // shortest path bewteen s (or sources) and v; null if no such path
    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<Integer>();
        int x;
        for (x = v; distTo[x] != 0; x = edgeTo[x])
            path.push(x);
        path.push(x);
        return path;
    }


    // check optimality conditions for single source
    private boolean check(Graph G, int s) {

        // check that the distance of s = 0
        if (distTo[s] != 0) {
            StdOut.println("distance of source " + s + " to itself = " + distTo[s]);
            return false;
        }

        // check that for each edge v-w dist[w] <= dist[v] + 1
        // provided v is reachable from s
        for (int v = 0; v < G.V(); v++) {
            for (int w : G.adj(v)) {
                if (hasPathTo(v) != hasPathTo(w)) {
                    StdOut.println("edge " + v + "-" + w);
                    StdOut.println("hasPathTo(" + v + ") = " + hasPathTo(v));
                    StdOut.println("hasPathTo(" + w + ") = " + hasPathTo(w));
                    return false;
                }
                if (hasPathTo(v) && (distTo[w] > distTo[v] + 1)) {
                    StdOut.println("edge " + v + "-" + w);
                    StdOut.println("distTo[" + v + "] = " + distTo[v]);
                    StdOut.println("distTo[" + w + "] = " + distTo[w]);
                    return false;
                }
            }
        }

        // check that v = edgeTo[w] satisfies distTo[w] + distTo[v] + 1
        // provided v is reachable from s
        for (int w = 0; w < G.V(); w++) {
            if (!hasPathTo(w) || w == s) continue;
            int v = edgeTo[w];
            if (distTo[w] != distTo[v] + 1) {
                StdOut.println("shortest path edge " + v + "-" + w);
                StdOut.println("distTo[" + v + "] = " + distTo[v]);
                StdOut.println("distTo[" + w + "] = " + distTo[w]);
                return false;
            }
        }

        return true;
    }


    // test client
    public static void main(String[] args) {
        In in = new In(args[0]);
        Graph G = new Graph(in);
        // StdOut.println(G);

        int s = Integer.parseInt(args[1]);
        BreadthFirstPaths bfs = new BreadthFirstPaths(G, s);

        for (int v = 0; v < G.V(); v++) {
            if (bfs.hasPathTo(v)) {
                StdOut.printf("%d to %d (%d):  ", s, v, bfs.distTo(v));
                for (int x : bfs.pathTo(v)) {
                    if (x == s) StdOut.print(x);
                    else        StdOut.print("-" + x);
                }
                StdOut.println();
            }

            else {
                StdOut.printf("%d to %d (-):  not connected\n", s, v);
            }

        }
    }


}
