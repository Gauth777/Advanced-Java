import java.util.*;

class TrieNode{
    HashMap<Character,TrieNode> children=new HashMap<>();
    HashMap<String,Integer> queries=new HashMap<>();
    boolean end=false;
}

class AutocompleteSystem{
    TrieNode root=new TrieNode();
    HashMap<String,Integer> frequency=new HashMap<>();

    public void updateFrequency(String query){
        int f=frequency.getOrDefault(query,0)+1;
        frequency.put(query,f);
        TrieNode node=root;
        for(char c:query.toCharArray()){
            node.children.putIfAbsent(c,new TrieNode());
            node=node.children.get(c);
            node.queries.put(query,f);
        }
        node.end=true;
    }

    public void search(String prefix){
        TrieNode node=root;
        for(char c:prefix.toCharArray()){
            if(!node.children.containsKey(c)){
                System.out.println("No suggestions");
                return;
            }
            node=node.children.get(c);
        }
        PriorityQueue<Map.Entry<String,Integer>> pq=new PriorityQueue<>((a,b)->b.getValue()-a.getValue());
        pq.addAll(node.queries.entrySet());
        int rank=1;
        while(!pq.isEmpty()&&rank<=10){
            Map.Entry<String,Integer> e=pq.poll();
            System.out.println(rank+". \""+e.getKey()+"\" ("+e.getValue()+" searches)");
            rank++;
        }
    }
}

public class Week1{
    public static void main(String[] args){
        AutocompleteSystem system=new AutocompleteSystem();
        system.updateFrequency("java tutorial");
        system.updateFrequency("javascript");
        system.updateFrequency("java download");
        system.updateFrequency("java tutorial");
        system.updateFrequency("java tutorial");
        System.out.println("search(\"jav\") ->");
        system.search("jav");
        system.updateFrequency("21 features");
        system.updateFrequency("21 features");
        system.updateFrequency("21 features");
        System.out.println("\nupdateFrequency(\"21 features\") -> Frequency: "+3);
    }
}
