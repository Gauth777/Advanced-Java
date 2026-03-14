import java.util.*;

class TokenBucket{
    int tokens,maxTokens;
    long lastRefillTime;
    int refillRate;
    TokenBucket(int maxTokens,int refillRate){
        this.tokens=maxTokens;
        this.maxTokens=maxTokens;
        this.refillRate=refillRate;
        this.lastRefillTime=System.currentTimeMillis();
    }
    void refill(){
        long now=System.currentTimeMillis();
        long elapsed=(now-lastRefillTime)/1000;
        int newTokens=(int)(elapsed*refillRate);
        if(newTokens>0){
            tokens=Math.min(maxTokens,tokens+newTokens);
            lastRefillTime=now;
        }
    }
    boolean allowRequest(){
        refill();
        if(tokens>0){
            tokens--;
            return true;
        }
        return false;
    }
}

class RateLimiter{
    HashMap<String,TokenBucket> clients=new HashMap<>();
    int maxTokens=1000;
    int refillRate=1000/3600;

    public String checkRateLimit(String clientId){
        clients.putIfAbsent(clientId,new TokenBucket(maxTokens,refillRate));
        TokenBucket bucket=clients.get(clientId);
        if(bucket.allowRequest()){
            return "Allowed ("+bucket.tokens+" requests remaining)";
        }
        return "Denied (0 requests remaining)";
    }

    public void getRateLimitStatus(String clientId){
        TokenBucket bucket=clients.get(clientId);
        int used=maxTokens-bucket.tokens;
        System.out.println("{used: "+used+", limit: "+maxTokens+"}");
    }
}

public class Week1{
    public static void main(String[] args){
        RateLimiter limiter=new RateLimiter();
        System.out.println("checkRateLimit(\"abc123\") -> "+limiter.checkRateLimit("abc123"));
        System.out.println("checkRateLimit(\"abc123\") -> "+limiter.checkRateLimit("abc123"));
        for(int i=0;i<998;i++) limiter.checkRateLimit("abc123");
        System.out.println("checkRateLimit(\"abc123\") -> "+limiter.checkRateLimit("abc123"));
        limiter.getRateLimitStatus("abc123");
    }
}
