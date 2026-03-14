import java.util.*;

class PlagiarismDetector {

    private HashMap<String, Set<String>> ngramIndex;
    private HashMap<String, String> documents;
    private int n;

    public PlagiarismDetector(int n) {
        this.n = n;
        ngramIndex = new HashMap<>();
        documents = new HashMap<>();
    }

    public void addDocument(String docId, String text) {

        documents.put(docId, text);

        List<String> ngrams = generateNgrams(text);

        for (String gram : ngrams) {

            ngramIndex.putIfAbsent(gram, new HashSet<>());

            ngramIndex.get(gram).add(docId);
        }
    }

    public void analyzeDocument(String docId, String text) {

        List<String> ngrams = generateNgrams(text);

        System.out.println("Extracted " + ngrams.size() + " n-grams");

        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String gram : ngrams) {

            if (ngramIndex.containsKey(gram)) {

                for (String otherDoc : ngramIndex.get(gram)) {

                    matchCount.put(otherDoc,
                            matchCount.getOrDefault(otherDoc, 0) + 1);
                }
            }
        }

        for (Map.Entry<String, Integer> entry : matchCount.entrySet()) {

            String otherDoc = entry.getKey();
            int matches = entry.getValue();

            double similarity = (matches * 100.0) / ngrams.size();

            System.out.println("Found " + matches + " matching n-grams with \"" + otherDoc + "\"");
            System.out.println("Similarity: " + String.format("%.1f", similarity) + "%");

            if (similarity > 50) {
                System.out.println("PLAGIARISM DETECTED");
            }

            System.out.println();
        }

        addDocument(docId, text);
    }

    private List<String> generateNgrams(String text) {

        List<String> grams = new ArrayList<>();

        String[] words = text.split("\\s+");

        for (int i = 0; i <= words.length - n; i++) {
            StringBuilder gram = new StringBuilder();
            for (int j = 0; j < n; j++) {
                gram.append(words[i + j]);
                if (j < n - 1) gram.append(" ");
            }

            grams.add(gram.toString());
        }

        return grams;
    }
}

public class Week1{

    public static void main(String[] args) {

        PlagiarismDetector detector = new PlagiarismDetector(5);
        String doc1 = "machine learning enables computers to learn from data and improve performance automatically";
        String doc2 = "machine learning enables systems to learn from data and improve automatically";
        detector.addDocument("essay_089.txt", doc1);
        detector.analyzeDocument("essay_123.txt", doc2);
    }
}
