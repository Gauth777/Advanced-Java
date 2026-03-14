import java.util.*;

class Transaction{
    int id,amount;String merchant,account,time;
    Transaction(int id,int amount,String merchant,String account,String time){
        this.id=id;this.amount=amount;this.merchant=merchant;this.account=account;this.time=time;
    }
}

class TransactionAnalyzer{
    List<Transaction> list=new ArrayList<>();

    public void add(Transaction t){list.add(t);}

    public void findTwoSum(int target){
        HashMap<Integer,Transaction> map=new HashMap<>();
        for(Transaction t:list){
            int c=target-t.amount;
            if(map.containsKey(c)){
                System.out.println("TwoSum -> ["+map.get(c).id+", "+t.id+"]");
                return;
            }
            map.put(t.amount,t);
        }
        System.out.println("No pair found");
    }

    public void detectDuplicates(){
        HashMap<String,List<Transaction>> map=new HashMap<>();
        for(Transaction t:list){
            String key=t.amount+"-"+t.merchant;
            map.putIfAbsent(key,new ArrayList<>());
            map.get(key).add(t);
        }
        for(Map.Entry<String,List<Transaction>> e:map.entrySet()){
            if(e.getValue().size()>1){
                List<String> acc=new ArrayList<>();
                for(Transaction t:e.getValue())acc.add(t.account);
                String[] p=e.getKey().split("-");
                System.out.println("{amount:"+p[0]+", merchant:\""+p[1]+"\", accounts:"+acc+"}");
            }
        }
    }

    public void findKSum(int k,int target){
        List<Integer> ids=new ArrayList<>();
        dfs(0,k,target,ids);
    }

    void dfs(int i,int k,int target,List<Integer> ids){
        if(k==0&&target==0){
            System.out.println("KSum -> "+ids);return;
        }
        if(i>=list.size()||k<0||target<0)return;
        ids.add(list.get(i).id);
        dfs(i+1,k-1,target-list.get(i).amount,ids);
        ids.remove(ids.size()-1);
        dfs(i+1,k,target,ids);
    }
}

public class Week1{
    public static void main(String[] args){
        TransactionAnalyzer a=new TransactionAnalyzer();
        a.add(new Transaction(1,500,"Store A","acc1","10:00"));
        a.add(new Transaction(2,300,"Store B","acc2","10:15"));
        a.add(new Transaction(3,200,"Store C","acc3","10:30"));
        a.add(new Transaction(4,500,"Store A","acc2","10:45"));
        a.findTwoSum(500);
        a.detectDuplicates();
        a.findKSum(3,1000);
    }
}
