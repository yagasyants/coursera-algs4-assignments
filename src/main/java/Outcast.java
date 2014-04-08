import java.util.HashMap;
import java.util.Map;

public class Outcast {
    private static final int UNFOUND = -1;
    private static final String KEY_SEPARATOR = "-";
    private WordNet wordnet;

    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    public String outcast(String[] nouns) {
        Map<String, Integer> distances = new HashMap<>();
        String outCast = "";
        int maxDist = UNFOUND;

        for (String nounToCheck : nouns) {
            int distance = 0;
            for (String nounToCompare : nouns) {
                distance += getDistance(nounToCheck, nounToCompare, distances);
            }
            if (distance > maxDist) {
                outCast = nounToCheck;
                maxDist = distance;
            }
        }

        return outCast;
    }

    private int getDistance(String nounA, String nounB,
            Map<String, Integer> distances) {
        String key = createKey(nounA, nounB);
        if (!distances.containsKey(key)) {
            distances.put(key, wordnet.distance(nounA, nounB));
        }
        return distances.get(key);
    }

    private String createKey(String nounA, String nounB) {
        if (nounA.compareTo(nounB) > 0) {
            return nounA + KEY_SEPARATOR + nounB;
        } else {
            return nounB + KEY_SEPARATOR + nounA;
        }
    }

}
