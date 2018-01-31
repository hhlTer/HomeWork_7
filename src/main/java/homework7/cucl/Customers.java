package homework7.cucl;

import java.util.ArrayList;
import java.util.List;

public class Customers {
    public List<Order> orderList = new ArrayList<>();
    public void addClient(Order client){ orderList.add(client);}

    public static class Order {
        public String name;
        public KindOfFruit type;
        public int count;
    }
}
