import java.util.*;

class DNSEntry {
    String ip;
    long expiryTime;

    DNSEntry(String ip, long ttl) {
        this.ip = ip;
        this.expiryTime = System.currentTimeMillis() + ttl * 1000;
    }

    boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

class DNSCache {

    private LinkedHashMap<String, DNSEntry> cache;
    private int capacity;
    private int hits;
    private int misses;

    public DNSCache(int capacity) {

        this.capacity = capacity;

        cache = new LinkedHashMap<String, DNSEntry>(capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > DNSCache.this.capacity;
            }
        };
    }

    public String resolve(String domain) {

        DNSEntry entry = cache.get(domain);

        if (entry != null) {

            if (!entry.isExpired()) {
                hits++;
                return "Cache HIT -> " + entry.ip;
            }

            cache.remove(domain);
        }

        misses++;

        String ip = queryUpstream(domain);

        cache.put(domain, new DNSEntry(ip, 300));

        return "Cache MISS -> Query upstream -> " + ip + " (TTL: 300s)";
    }

    private String queryUpstream(String domain) {

        Random r = new Random();

        return "172.217.14." + (200 + r.nextInt(50));
    }

    public String getCacheStats() {

        int total = hits + misses;

        double hitRate = total == 0 ? 0 : ((double) hits / total) * 100;

        return "Hit Rate: " + String.format("%.1f", hitRate) + "%";
    }
}

public class Week1 {

    public static void main(String[] args) throws Exception {

        DNSCache dns = new DNSCache(5);

        System.out.println("resolve(\"google.com\") -> " + dns.resolve("google.com"));

        System.out.println("resolve(\"google.com\") -> " + dns.resolve("google.com"));

        Thread.sleep(1000);

        System.out.println("resolve(\"google.com\") -> " + dns.resolve("google.com"));

        System.out.println("getCacheStats() -> " + dns.getCacheStats());
    }
}
