import stdlib.StdIn;
import stdlib.StdOut;

public class Outcast {
    private final WordNet wordnet;

    // Constructs an Outcast object given the WordNet semantic lexicon.
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // Returns the outcast noun from nouns.
    public String outcast(String[] nouns) {
        int maxDistance = -1;
        String outcast = null;

        for (String nounA : nouns) {
            int distanceSum = 0;
            for (String nounB : nouns) {
                if (!nounA.equals(nounB)) {
                    distanceSum += wordnet.distance(nounA, nounB);
                }
            }
            if (distanceSum > maxDistance) {
                maxDistance = distanceSum;
                outcast = nounA;
            }
        }
        return outcast;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        String[] nouns = StdIn.readAllStrings();
        String outcastNoun = outcast.outcast(nouns);
        for (String noun : nouns) {
            StdOut.print(noun.equals(outcastNoun) ? "*" + noun + "* " : noun + " ");
        }
        StdOut.println();
    }
}
