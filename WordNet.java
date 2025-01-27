import dsa.DiGraph;
import dsa.SeparateChainingHashST;
import dsa.Set;
import stdlib.In;
import stdlib.StdOut;

public class WordNet {
    private final SeparateChainingHashST<String, Set<Integer>> st;
    private final SeparateChainingHashST<Integer, String> rst;
    private final ShortestCommonAncestor sca;

    // Constructs a WordNet object given the names of the input (synset and hypernym) files.
    public WordNet(String synsets, String hypernyms) {
        //corner cases
        if (synsets == null) {
            throw new NullPointerException("synsets is null");
        }
        if (hypernyms == null) {
            throw new NullPointerException("hypernyms is null");
        }

        st = new SeparateChainingHashST<>();
        rst = new SeparateChainingHashST<>();

        In synsetIn= new In(synsets);
        while (synsetIn.hasNextLine()) {
            String line = synsetIn.readLine();
            String[] fields = line.split(",");
            int id = Integer.parseInt(fields[0]);
            String synset = fields[1];
            rst.put(id, synset);

            String[] nouns = synset.split(" ");
            for (String noun : nouns) {
                Set<Integer> set = st.get(noun);
                if (set == null) {
                    set = new Set<>();
                    st.put(noun, set);
                }
                set.add(id);
            }
        }
        synsetIn.close();

        DiGraph G = new DiGraph(rst.size());
        In hypernymIn = new In(hypernyms);
        while (hypernymIn.hasNextLine()) {
            String line = hypernymIn.readLine();
            String[] fields = line.split(",");
            int synsetId= Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                G.addEdge(synsetId, Integer.parseInt(fields[i]));
            }
        }
        hypernymIn.close();

        sca = new ShortestCommonAncestor(G);
    }

    // Returns all WordNet nouns.
    public Iterable<String> nouns() {
        return st.keys();
    }

    // Returns true if the given word is a WordNet noun, and false otherwise.
    public boolean isNoun(String word) {
        // corner case
        if (word == null) {
            throw new NullPointerException("word is null");
        }
        return st.contains(word);
    }

    // Returns a synset that is a shortest common ancestor of noun1 and noun2.
    public String sca(String noun1, String noun2) {
        // corner cases
        if (noun1 == null) {
            throw new NullPointerException("noun1 is null");
        }
        if (noun2 == null) {
            throw new NullPointerException("noun2 is null");
        }
        if (!isNoun(noun1)) {
            throw new IllegalArgumentException("noun1 is not a noun");
        }
        if (!isNoun(noun2)) {
            throw new IllegalArgumentException("noun2 is not a noun");
        }

        Set<Integer> set1 = st.get(noun1);
        Set<Integer> set2 = st.get(noun2);
        int ancestorId = sca.ancestor(set1, set2);
        return rst.get(ancestorId);
    }

    // Returns the length of the shortest ancestral path between noun1 and noun2.
    public int distance(String noun1, String noun2) {
        // corner cases
        if (noun1 == null) {
            throw new NullPointerException("noun1 is null");
        }
        if (noun2 == null) {
            throw new NullPointerException("noun2 is null");
        }
        if (!isNoun(noun1)) {
            throw new IllegalArgumentException("noun1 is not a noun");
        }
        if (!isNoun(noun2)) {
            throw new IllegalArgumentException("noun2 is not a noun");
        }

        Set<Integer> set1 = st.get(noun1);
        Set<Integer> set2 = st.get(noun2);
        return sca.length(set1, set2);
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        String word1 = args[2];
        String word2 = args[3];
        int nouns = 0;
        for (String noun : wordnet.nouns()) {
            nouns++;
        }
        StdOut.printf("# of nouns = %d\n", nouns);
        StdOut.printf("isNoun(%s)? %s\n", word1, wordnet.isNoun(word1));
        StdOut.printf("isNoun(%s)? %s\n", word2, wordnet.isNoun(word2));
        StdOut.printf("isNoun(%s %s)? %s\n", word1, word2, wordnet.isNoun(word1 + " " + word2));
        StdOut.printf("sca(%s, %s) = %s\n", word1, word2, wordnet.sca(word1, word2));
        StdOut.printf("distance(%s, %s) = %s\n", word1, word2, wordnet.distance(word1, word2));
    }
}
