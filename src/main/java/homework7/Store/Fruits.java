package homework7.Store;

import java.math.BigDecimal;
import java.util.Date;

class Fruits {

    private KindOfFruit type;
    private Date date;
    private BigDecimal price;
    private int shelfLife;

    Fruits(KindOfFruit type, Date date, BigDecimal price, int shelfLife) {
        this.type = type;
        this.date = date;
        this.price = price;
        this.shelfLife = shelfLife;
    }

    Fruits(){
        type = null;
        date = new Date();
        price = new BigDecimal(0);
        shelfLife = 0;
    }

}
