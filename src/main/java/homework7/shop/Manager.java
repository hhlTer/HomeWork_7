package homework7.shop;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

public class Manager {
    public static void main(String[] args) throws FileNotFoundException {
        Store store = new Store();
        Fruits fruits;

/**
 *       create random delivery.
 *        Date - current day
 *        left time - random of 20 day
 *        price - random of 50
 */
//---------------------1--------------------------------------
//        Delivery delivery = newDelivery();
//        File file = new File("randomDelivery.dat");
//        saveDelivery(file, delivery);
//------------------------------------------------------------

/**
 *        add delivery to file store.dat
 */
//---------------------2--------------------------------------
//        store.addFruits(file.getName());
//        store.save("store.dat");
//------------------------------------------------------------

/**
 *        load from store.dat to Store class (fruitList)
 */
//---------------------3--------------------------------------
//        store.load("store.dat");
//------------------------------------------------------------

/**
 *          create order file (order.dat)
 */
//---------------------4--------------------------------------
//    addClients();
//------------------------------------------------------------

/**
 *        sell (using file clientGroup.dat)
 */
//---------------------5--------------------------------------
//        store.sell("clientGroup.dat");
//------------------------------------------------------------

}

    static void saveDelivery(File file, Delivery del){
        String s = JSON.toJSONString(del);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            file.createNewFile();
            byte[] b = s.getBytes();
            fos.write(b);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static Delivery newDelivery(){
        Delivery delivery = new Delivery();
        Fruits fruits;
        Random random = new Random();
        for (int i = 0; i < 200; i++) {
            fruits = new Fruits();
            fruits.date = new Date();
            fruits.type = KindOfFruit.values()[random.nextInt(KindOfFruit.values().length)];
            fruits.shelfLife = random.nextInt(20);
            fruits.price = new BigDecimal(random.nextInt(50));
            delivery.addDelivery(fruits);
        }
        return delivery;
    }

    static void addClients(){
        ClientGroup clientGroup = new ClientGroup();
        Order order = new Order();
        order.name = "Sasha";
        order.type = KindOfFruit.APPLE;
        order.count = 22;
        clientGroup.addClient(order);

        order = new Order();
        order.name = "Valeryi";
        order.type = KindOfFruit.PEAR;
        order.count = 13;
        clientGroup.addClient(order);

        order = new Order();
        order.name = "Olena";
        order.type = KindOfFruit.BANANAS;
        order.count = 20;
        clientGroup.addClient(order);

        String j = JSON.toJSONString(clientGroup);
        File file = new File("order.dat");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            byte[] b = j.getBytes();
            fos.write(b);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
