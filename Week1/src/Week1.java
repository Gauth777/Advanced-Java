import java.util.*;

class FlashSaleInventory {

    private HashMap<String, Integer> stock;
    private HashMap<String, Queue<Integer>> waitingList;

    public FlashSaleInventory() {
        stock = new HashMap<>();
        waitingList = new HashMap<>();
    }

    public void addProduct(String productId, int quantity) {
        stock.put(productId, quantity);
        waitingList.put(productId, new LinkedList<>());
    }

    public int checkStock(String productId) {
        return stock.getOrDefault(productId, 0);
    }

    public synchronized String purchaseItem(String productId, int userId) {

        int currentStock = stock.getOrDefault(productId, 0);

        if (currentStock > 0) {
            stock.put(productId, currentStock - 1);
            return "Success, " + (currentStock - 1) + " units remaining";
        }

        Queue<Integer> queue = waitingList.get(productId);
        queue.add(userId);
        return "Added to waiting list, position #" + queue.size();
    }
}

public class Week1 {

    public static void main(String[] args) {

        FlashSaleInventory system = new FlashSaleInventory();

        system.addProduct("IPHONE15_256GB", 100);

        System.out.println("checkStock(\"IPHONE15_256GB\") -> "
                + system.checkStock("IPHONE15_256GB") + " units available");

        System.out.println("purchaseItem(\"IPHONE15_256GB\", userId=12345) -> "
                + system.purchaseItem("IPHONE15_256GB", 12345));

        System.out.println("purchaseItem(\"IPHONE15_256GB\", userId=67890) -> "
                + system.purchaseItem("IPHONE15_256GB", 67890));

        for(int i=0;i<98;i++){
            system.purchaseItem("IPHONE15_256GB", i);
        }

        System.out.println("purchaseItem(\"IPHONE15_256GB\", userId=99999) -> "
                + system.purchaseItem("IPHONE15_256GB", 99999));
    }
}
