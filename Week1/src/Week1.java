import java.util.*;

class UsernameSystem {

    private HashMap<String, Integer> users;
    private HashMap<String, Integer> attemptFrequency;
    private int nextUserId;

    public UsernameSystem() {
        users = new HashMap<>();
        attemptFrequency = new HashMap<>();
        nextUserId = 1;
    }

    public void register(String username) {
        if (!users.containsKey(username)) {
            users.put(username, nextUserId++);
            System.out.println("User registered: " + username);
        } else {
            System.out.println("Username already exists.");
        }
    }

    public boolean checkAvailability(String username) {
        attemptFrequency.put(username,
                attemptFrequency.getOrDefault(username, 0) + 1);
        return !users.containsKey(username);
    }

    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            String suggestion = username + i;
            if (!users.containsKey(suggestion))
                suggestions.add(suggestion);
        }

        if (username.contains("_")) {
            String alt = username.replace("_", ".");
            if (!users.containsKey(alt))
                suggestions.add(alt);
        }

        String year = username + "2025";
        if (!users.containsKey(year))
            suggestions.add(year);

        return suggestions;
    }

    public String getMostAttempted() {

        String maxUser = null;
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : attemptFrequency.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                maxUser = entry.getKey();
            }
        }

        return maxUser + " (" + maxCount + " attempts)";
    }
}

public class Week1 {

    public static void main(String[] args) {

        UsernameSystem system = new UsernameSystem();

        system.register("john_doe");
        system.register("admin");
        system.register("alex");

        System.out.println();

        System.out.println("checkAvailability(\"john_doe\") -> "
                + system.checkAvailability("john_doe"));

        System.out.println("checkAvailability(\"jane_smith\") -> "
                + system.checkAvailability("jane_smith"));

        System.out.println();

        System.out.println("suggestAlternatives(\"john_doe\") -> "
                + system.suggestAlternatives("john_doe"));

        System.out.println();

        system.checkAvailability("admin");
        system.checkAvailability("admin");
        system.checkAvailability("admin");
        system.checkAvailability("john_doe");

        System.out.println("getMostAttempted() -> "
                + system.getMostAttempted());
    }
}
