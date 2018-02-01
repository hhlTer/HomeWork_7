package homework7.cucl;

import java.math.BigDecimal;
import java.util.Date;

public class Fruits {

    public KindOfFruit type;
    public Date date;
    public BigDecimal price;
    public int shelfLife;

    public Fruits(KindOfFruit type, Date date, BigDecimal price, int shelfLife) {
        this.type = type;
        this.date = date;
        this.price = price;
        this.shelfLife = shelfLife;
    }

    public Fruits(){
        type = null;
        date = new Date();
        price = new BigDecimal(0);
        shelfLife = 0;
    }

    public String toString(){
        Date a = new Date(date.getTime() + (24*60*60*1000*shelfLife));
//        Calendar c = Calendar.getInstance();
        return String.format("%s, delivery date %s, expiration day %s, price %s",
                type, date.getDate() + "." + date.getMonth()+1,
                      a.getDate() + "." + a.getMonth()+1, price);
    }

}
