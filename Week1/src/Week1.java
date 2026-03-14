import java.util.*;

class Video{
    String id,data;
    Video(String id,String data){this.id=id;this.data=data;}
}

class MultiLevelCache{
    LinkedHashMap<String,Video> L1=new LinkedHashMap<>(16,0.75f,true);
    HashMap<String,Video> L2=new HashMap<>();
    HashMap<String,Video> L3=new HashMap<>();
    HashMap<String,Integer> access=new HashMap<>();
    int l1Hits=0,l2Hits=0,l3Hits=0;

    public Video getVideo(String id){
        if(L1.containsKey(id)){
            l1Hits++;
            System.out.println("L1 Cache HIT (0.5ms)");
            return L1.get(id);
        }
        if(L2.containsKey(id)){
            l2Hits++;
            System.out.println("L1 Cache MISS");
            System.out.println("L2 Cache HIT (5ms)");
            Video v=L2.get(id);
            promoteToL1(id,v);
            return v;
        }
        if(L3.containsKey(id)){
            l3Hits++;
            System.out.println("L1 Cache MISS");
            System.out.println("L2 Cache MISS");
            System.out.println("L3 Database HIT (150ms)");
            Video v=L3.get(id);
            L2.put(id,v);
            access.put(id,1);
            System.out.println("Added to L2 (access count: 1)");
            return v;
        }
        return null;
    }

    void promoteToL1(String id,Video v){
        L1.put(id,v);
        if(L1.size()>10000){
            Iterator<String> it=L1.keySet().iterator();
            it.next();it.remove();
        }
        System.out.println("Promoted to L1");
    }

    public void addToDatabase(Video v){L3.put(v.id,v);}

    public void getStatistics(){
        int total=l1Hits+l2Hits+l3Hits;
        double l1=(l1Hits*100.0)/(total==0?1:total);
        double l2=(l2Hits*100.0)/(total==0?1:total);
        double l3=(l3Hits*100.0)/(total==0?1:total);
        System.out.println("L1 Hit Rate "+String.format("%.1f",l1)+"%");
        System.out.println("L2 Hit Rate "+String.format("%.1f",l2)+"%");
        System.out.println("L3 Hit Rate "+String.format("%.1f",l3)+"%");
        System.out.println("Overall Hit Rate "+String.format("%.1f",(l1Hits+l2Hits+l3Hits)*100.0/(total==0?1:total))+"%");
    }
}

public class Week1{
    public static void main(String[] args){
        MultiLevelCache c=new MultiLevelCache();
        c.addToDatabase(new Video("video_123","dataA"));
        c.addToDatabase(new Video("video_999","dataB"));

        System.out.println("getVideo(\"video_123\")");
        c.getVideo("video_123");
        System.out.println("\ngetVideo(\"video_123\") second request");
        c.getVideo("video_123");

        System.out.println("\ngetVideo(\"video_999\")");
        c.getVideo("video_999");

        System.out.println("\nStatistics:");
        c.getStatistics();
    }
}
