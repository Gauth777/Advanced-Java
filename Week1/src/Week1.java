import java.util.*;

class AnalyticsSystem{
    HashMap<String,Integer> pageViews=new HashMap<>();
    HashMap<String,Set<String>> uniqueVisitors=new HashMap<>();
    HashMap<String,Integer> trafficSources=new HashMap<>();

    public void processEvent(String url,String userId,String source){
        pageViews.put(url,pageViews.getOrDefault(url,0)+1);
        uniqueVisitors.putIfAbsent(url,new HashSet<>());
        uniqueVisitors.get(url).add(userId);
        trafficSources.put(source,trafficSources.getOrDefault(source,0)+1);
    }

    public void getDashboard(){
        PriorityQueue<Map.Entry<String,Integer>> pq=new PriorityQueue<>((a,b)->b.getValue()-a.getValue());
        pq.addAll(pageViews.entrySet());
        System.out.println("Top Pages:");
        int rank=1;
        while(!pq.isEmpty()&&rank<=10){
            Map.Entry<String,Integer> e=pq.poll();
            String url=e.getKey();
            int views=e.getValue();
            int unique=uniqueVisitors.get(url).size();
            System.out.println(rank+". "+url+" - "+views+" views ("+unique+" unique)");
            rank++;
        }
        System.out.println("\nTraffic Sources:");
        for(Map.Entry<String,Integer> e:trafficSources.entrySet()){
            System.out.println(e.getKey()+" : "+e.getValue());
        }
    }
}

public class Week1{
    public static void main(String[] args){
        AnalyticsSystem system=new AnalyticsSystem();
        system.processEvent("/article/breaking-news","user_123","google");
        system.processEvent("/article/breaking-news","user_456","facebook");
        system.processEvent("/sports/championship","user_123","direct");
        system.processEvent("/sports/championship","user_789","google");
        system.processEvent("/article/breaking-news","user_123","google");
        system.getDashboard();
    }
}
