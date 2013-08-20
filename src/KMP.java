/***************************************************************
 *
 *  Compilation:  javac KMP.java
 *  Execution:    java KMP pattern text
 *
 *  Reads in two strings, the pattern and the input text, and
 *  searches for the pattern in the input text using the
 *  KMP algorithm.
 *
 *  % java KMP abracadabra abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad 
 *  pattern:               abracadabra          
 *
 *  % java KMP rab abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad 
 *  pattern:         rab
 *
 *  % java KMP bcara abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad 
 *  pattern:                                   bcara
 *
 *  % java KMP rabrabracad abacadabrabracabracadabrabrabracad 
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern:                        rabrabracad
 *
 *  % java KMP abacad abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern: abacad
 *
 ***************************************************************/

public class KMP {
    private final int R;       // the radix
    private int[][] dfa;       // the KMP automoton

    private char[] pattern;    // either the character array for the pattern
    private String pat;        // or the pattern string

    // create the DFA from a String
    public KMP(String pat) {
        this.R = 256; //the ascii numerical equivalent of a character could be from 1-256
        			  //So that many number of rows
        this.pat = pat;

        // build DFA from pattern
        int M = pat.length();
        
        dfa = new int[R][M];  //R possible inputs, M possible states 
        
        //dfa[65][2] means, if I am currently in State 2 and the incoming character is A, what state should this go to ?
        //note that dfa is built based on the pattern
        
        dfa[pat.charAt(0)][0] = 1; //I am in the very first state (initial state) and receive 
                                   //the first pattern character. Go to the next state.
        
          
        for (int X = 0, j = 1; j < M; j++) {  
        	   								//Start with fallback state X=0 initially
        	 								//Have to update missing transitions for states 1 to the end of pattern
        									//For j=0, dfa[pat.charAt(0)][0] = 1; takes care of success transition and the rest default to 0.
        									//So we don't worry about 0th state, in this for loop
           
        	for (int c = 0; c < R; c++) 
            { dfa[c][j] = dfa[c][X];  		// Copy mismatch cases. 
            } 								// Copy the whole column at X, to the current column position j
            							   
        	/*Override for the matching cases
        	States: 0-1-2-3
        	Pattern:"abc" a at 0 transitions to 1, b at 1 transitions to 2, c at 2 transitions to 3
        	pat.chatAt(j) at state j should trigger a transition to state j+1
        	 */
        	
        	dfa[pat.charAt(j)][j] = j+1;    //For matching cases, update the cell values
            							    //The cell value would be currentStateNo+1. So j+1
            								
        							
        									//If I had, received the same character at the fallback state, where would I have gone ? 
        									//That's the new fallback state.
            X = dfa[pat.charAt(j)][X];      // Update restart state. 
        } 
    } 

    // create the DFA from a character array over R-character alphabet
    public KMP(char[] pattern, int R) {
        this.R = R;
        this.pattern = new char[pattern.length];
        for (int j = 0; j < pattern.length; j++)
            this.pattern[j] = pattern[j];

        // build DFA from pattern
        int M = pattern.length;
        dfa = new int[R][M]; 
        dfa[pattern[0]][0] = 1; 
        for (int X = 0, j = 1; j < M; j++) {
            for (int c = 0; c < R; c++) 
                dfa[c][j] = dfa[c][X];     // Copy mismatch cases. 
            dfa[pattern[j]][j] = j+1;      // Set match case. 
            X = dfa[pattern[j]][X];        // Update restart state. 
        } 
    } 

    // return offset of first match; N if no match
    public int search(String txt) {

        // simulate operation of DFA on text
        int M = pat.length();
        int N = txt.length();
        int i, j;
        for (i = 0, j = 0; i < N && j < M; i++) { 
        								//Start from 0 as text position to txt.length()
        								//If you ever reach j=M, you have found a match, so stop
            j = dfa[txt.charAt(i)][j];
        }
        if (j == M) return i - M;    // found
        return N;                    // not found
    }


    // return offset of first match; N if no match
    public int search(char[] text) {

        // simulate operation of DFA on text
        int M = pattern.length;
        int N = text.length;
        int i, j;
        for (i = 0, j = 0; i < N && j < M; i++) {
            j = dfa[text[i]][j];
        }
        if (j == M) return i - M;    // found
        return N;                    // not found
    }


    // test client
    public static void main(String[] args) {
        String pat = args[0];
        String txt = args[1];
        char[] pattern = pat.toCharArray();
        char[] text    = txt.toCharArray();

        KMP kmp1 = new KMP(pat);
        int offset1 = kmp1.search(txt);

        KMP kmp2 = new KMP(pattern, 256);
        int offset2 = kmp2.search(text);

        // print results
        StdOut.println("text:    " + txt);

        StdOut.print("pattern: ");
        for (int i = 0; i < offset1; i++)
            StdOut.print(" ");
        StdOut.println(pat);

        StdOut.print("pattern: ");
        for (int i = 0; i < offset2; i++)
            StdOut.print(" ");
        StdOut.println(pat);
    }
}
