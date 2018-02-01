package homework7.cucl;

import homework7.shop.Store;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StoreDB extends Store {
    public List<Fruits> fruitsList = new ArrayList<>();
    public BigDecimal moneyBalance = new BigDecimal(0);
}
