package homework7.cucl;

import java.util.ArrayList;
import java.util.List;

public class Delivery {
    public List<Fruits> fruitsList = new ArrayList<>();
    public void addDelivery(Fruits fruits){
        fruitsList.add(fruits);
    }

}
