package homework7.Store;

import java.math.BigDecimal;
import java.util.Date;

public class Manager {
    public static void main(String[] args) {
        Store store = new Store();
        Fruits fruits = new Fruits(KindOfFruit.APPLE, new Date(), new BigDecimal(11), 10);
        store.addFruits(fruits);
        fruits = new Fruits(KindOfFruit.PEACH, new Date(), new BigDecimal(23), 15);
        store.addFruits(fruits);
        store.save("firstAttmpt.dat");
    }
}
