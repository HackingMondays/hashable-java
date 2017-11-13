package hm;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;

public class Hashable {
    public static Integer hashWord(String word) {
        return word.length();
    }

    public static void main(String[] args) {
        start("load words");
        ArrayList<String> words = loadWords();
        end("load words");

        HashMap<Integer, ArrayList<String>> results = new HashMap<>();

        start("iterate over words");
        for (String word : words) {
            Integer hash = Hashable.hashWord(word);
            if (results.containsKey(hash)) {
                results.get(hash).add(word);
            } else {
                ArrayList<String> innerWords = new ArrayList<>();
                innerWords.add(word);
                results.put(hash, innerWords);
            }
        }
        end("iterate over words");

        if (results.size() < words.size()) {
            int collisionCount = words.size() - results.size();
            for (Map.Entry<Integer, ArrayList<String>> entry : results.entrySet()) {
                ArrayList<String> innerWords = entry.getValue();
                if (innerWords.size() > 1) {
                    List<String> sub = innerWords.subList(0, Math.min(10, innerWords.size()));
                    System.out.println(
                            "Found: " + SoutColors.WHITE +
                                    String.join(", ", sub) + (words.size() > 10 ? ", ..." : "") +
                                    SoutColors.RESET +
                                    " for " + SoutColors.CYAN + entry.getKey() + SoutColors.RESET +
                                    " so " + SoutColors.RED + innerWords.size() + SoutColors.RESET + " collisions"
                    );
                }
            }
            System.out.println("There was a total of " + SoutColors.RED + collisionCount + SoutColors.RESET + " collisions");
        } else {
            System.out.println(SoutColors.GREEN + "WOW, no collision" + SoutColors.RESET);
        }
    }

    static private ArrayList<String> loadWords() {
        try {
            ClassLoader classLoader = Hashable.class.getClassLoader();
            InputStream stream = classLoader.getResourceAsStream("words_dictionary.json");
            String s = IOUtils.toString(stream, Charset.forName("UTF-8"));
            JSONObject jsonObject = new JSONObject(s);
            Iterator<String> keys = jsonObject.keys();
            ArrayList<String> words = new ArrayList<>();
            keys.forEachRemaining(words::add);
            return words;
        } catch (Throwable e) {
            return new ArrayList<>();
        }
    }

    private static HashMap<String, Long> measurements = new HashMap<>();

    private static void start(String benchmark) {
        measurements.put(benchmark, System.currentTimeMillis());
    }

    private static void end(String benchmark) {
        if (measurements.containsKey(benchmark)) {
            long startTime = measurements.remove(benchmark);
            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println(benchmark + " took: " + SoutColors.GREEN + elapsedTime + SoutColors.RESET + "ms.");
        }
    }
}


enum SoutColors {
    BLUE("\u001B[34m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    WHITE("\u001B[37m"),
    YELLOW("\u001B[33m"),
    CYAN("\u001B[36m"),
    RESET("\u001B[0m");

    public final String value;

    SoutColors(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
