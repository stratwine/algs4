/*************************************************************************
 *  Compilation:  javac Graph.java        
 *  Execution:    java Graph input.txt
 *  Dependencies: Bag.java In.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/41undirected/tinyG.txt
 *
 *  A graph, implemented using an array of sets.
 *  Parallel edges and self-loops allowed.
 *
 *  % java Graph tinyG.txt
 *  13 vertices, 13 edges 
 *  0: 6 2 1 5 
 *  1: 0 
 *  2: 0 
 *  3: 5 4 
 *  4: 5 6 3 
 *  5: 3 4 0 
 *  6: 0 4 
 *  7: 8 
 *  8: 7 
 *  9: 11 10 12 
 *  10: 9 
 *  11: 9 12 
 *  12: 11 9 
 *
 *  % java Graph mediumG.txt
 *  250 vertices, 1273 edges 
 *  0: 225 222 211 209 204 202 191 176 163 160 149 114 97 80 68 59 58 49 44 24 15 
 *  1: 220 203 200 194 189 164 150 130 107 72 
 *  2: 141 110 108 86 79 51 42 18 14 
 *  ...
 *  
 *************************************************************************/


/**
 *  The <tt>Graph</tt> class represents an undirected graph of vertices
 *  named 0 through V-1.
 *  It supports the following operations: add an edge to the graph,
 *  iterate over all of the neighbors adjacent to a vertex.
 *  Parallel edges and self-loops are permitted.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/51undirected">Section 5.1</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */

/**
 * Here, a Graph is made by an array of Bag of edges
 * Each Bag contains a set of connected edges
 * Say countries in a continent are represented as a Graph.  (Ignore islands)
 * Then we would have 7 bags. Connected countries are marked by edges
 * There is no separate Vertex class
 */

/**
 * Absolutely necessary: No of vertices in the Graph.
 * There may be any number of connections
 * -> There may be any number of edges
 * This adjacency list contains Bag<Integer>.
 * The adjacency list could as well contain Set<Integer>
 * 
 * What's the significance of the 'Integer' here ?
 * Vertices are named by numbers
 * 
 * addEdge is supported. Requires two parameters 
 * Two vertices that need the connection
 * Take the Collection correponding to v. add w to that
 * Take the Collection corresponding to w. add v to that
 */

public class Graph {
    private final int V;
    private int E;
    private Bag<Integer>[] adj; // the array subscript refers to the vertex number.
    							// the Bag<Integer> contained in the location, consists of edges adjacent to the vertex
    							// In effect, this forms an adjacency Bag 
    							//(not very different an array of LinkedList | array of ArrayList)
    							// Infact, Bag is itself implemented with a self referential next node - making it a linked-list
    							//It differs from linked list in that, it does not allow to remove. 
    							// Inserts always at head. (this implementation)

       //Works when the vertices names are numbers (use array subscript)
       //What if I wanted each vertex to be named as a String ?
       //Adjacency matrix won't help
       //Still possible to create an adjacency list
       //HashMap<String,LinkedList<String>> ?
   /**
     * Create an empty graph with V vertices.
     * @throws java.lang.IllegalArgumentException if V < 0
     */
    public Graph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
        this.V = V;
        this.E = 0;
        adj = (Bag<Integer>[]) new Bag[V];
        
        /*
         * For each vertex,create a bag to hold the adjacent vertices
         */
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<Integer>(); 
        }
    }

   /**
     * Create a random graph with V vertices and E edges.
     * Expected running time is proportional to V + E.
     * @throws java.lang.IllegalArgumentException if either V < 0 or E < 0
     */
    public Graph(int V, int E) {
        this(V);
        if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
        for (int i = 0; i < E; i++) {
            int v = (int) (Math.random() * V);
            int w = (int) (Math.random() * V);
            addEdge(v, w);
        }
    }

   /**  
     * Create a digraph from input stream.
     */
    public Graph(In in) {
        this(in.readInt());
        int E = in.readInt();
        for (int i = 0; i < E; i++) {
            int v = in.readInt();
            int w = in.readInt();
            addEdge(v, w);
        }
    }

   /**
     * Copy constructor.
     */
    public Graph(Graph G) {
        this(G.V());
        this.E = G.E();
        for (int v = 0; v < G.V(); v++) {
            // reverse so that adjacency list is in same order as original
            Stack<Integer> reverse = new Stack<Integer>();
            for (int w : G.adj[v]) {
                reverse.push(w);
            }
            for (int w : reverse) {
                adj[v].add(w);
            }
        }
    }

   /**
     * Return the number of vertices in the graph.
     */
    public int V() { return V; }

   /**
     * Return the number of edges in the graph.
     */
    public int E() { return E; }


   /**
     * Add the undirected edge v-w to graph.
     * @throws java.lang.IndexOutOfBoundsException unless both 0 <= v < V and 0 <= w < V
     */
    
    /* Add edge between India and Nepal */
    public void addEdge(int v, int w) {
        if (v < 0 || v >= V) throw new IndexOutOfBoundsException();
        if (w < 0 || w >= V) throw new IndexOutOfBoundsException();
        E++;
        adj[v].add(w); // Add Nepal as an adjacent country to India 
        adj[w].add(v); // Add India as an adjacent country to Nepal
    } 

    //Can do a second invocation of the addEdge() method to add an edge between India and China
    //So the bag corresponding to India, would contain both these countries (Nepal,China)
    //Bag supports Iteration, so you can iterate
    
   /**
     * Return the list of neighbors of vertex v as in Iterable.
     * @throws java.lang.IndexOutOfBoundsException unless 0 <= v < V
     */
    public Iterable<Integer> adj(int v) {
        if (v < 0 || v >= V) throw new IndexOutOfBoundsException();
        return adj[v];
    }


   /**
     * Return a string representation of the graph.
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        String NEWLINE = System.getProperty("line.separator");
        s.append(V + " vertices, " + E + " edges " + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (int w : adj[v]) {
                s.append(w + " ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }


   /**
     * Test client.
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Graph G = new Graph(in);
        StdOut.println(G);
    }

}
