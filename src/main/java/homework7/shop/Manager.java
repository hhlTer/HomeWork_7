package homework7.shop;

import com.alibaba.fastjson.JSON;
import homework7.cucl.Customers;
import homework7.cucl.Delivery;
import homework7.cucl.KindOfFruit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Manager {
    public static void main(String[] args) throws FileNotFoundException {
        fillAllFiles();

    }
    static List<Delivery.Fruits> countOfAvailableFruit(File file, Date date, KindOfFruit type) throws FileNotFoundException{
        Store store = new Store();
        store.load(file.getName());
//        Date date = new Date();
//        date = new Date(date.getTime() + (24*60*60*1000*50));
        List<Delivery.Fruits> list = store.getAvailableFruits(date, KindOfFruit.APPLE);
        return list;
    }

    /** fillAllFiles():
     ** create and fill:
     *      store1.dat
     *      store2.dat : Contain JSON with data of shop - ArrayList<Fruits> + moneyBalance
     *
     *      randomDelivery1.dat : for store1
     *      randomDelivery2.dat : for store2. Contain JSON with deliver of fruit - ArrayList<Fruits>
     *          (using for fill stores in save(String patchFile) method)
     *
     *      order1.dat : for store1
     *      order2.dat : for store2. Contain JSON with customers - ArrayList<Order>
     *          (using in sell(String orderFile) method)
     *
     ** Performs sale for store1 and store2 and rewrites store1.dat and store2.dat with new data (order SUBTRACT shop)
     *  (uses sell(String orderFileName) method.
     *  +++ old Store.dat files are saves in .bak files
     *  +++ If the store does not have enough fruit for ordering - a message with information is displayed.
     *
     * @throws FileNotFoundException
     */
    static void fillAllFiles() throws FileNotFoundException{
//============================================================
        Store store1 = new Store();
        Store store2 = new Store();
/**
 *        create random delivery.
 *        Date - current day
 *        left time - random of 20 day
 *        price - random of 50
 */
//---------------------1--------------------------------------
        Delivery delivery = newDelivery();
        File file = new File("randomDelivery1.dat");
        saveDelivery(file, delivery);

        delivery = newDelivery();
        file = new File("randomDelivery2.dat");
        saveDelivery(file, delivery);
//------------------------------------------------------------
/**
 *        add delivery to file store.dat
 */
 //---------------------2--------------------------------------
        store1.addFruits(file.getName());
        store1.save("store1.dat");

        store2.addFruits(file.getName());
        store2.save("store2.dat");
//-------------------------------------------------------------
/**
 *        load from store.dat to Store class (fruitList)
 */
//------------------------------------------------------------
//---------------------3--------------------------------------
        store1.load("store1.dat");
        store2.load("store2.dat");
//------------------------------------------------------------
/**
 *          create order file (order.dat)
 */
//------------------------------------------------------------
//---------------------4--------------------------------------
        addClients("order1.dat");
        addClients("order2.dat");
//------------------------------------------------------------
/**
 *        sell (using file clientGroup.dat)
 */
//------------------------------------------------------------
//---------------------5--------------------------------------
        store1.sell("order1.dat");
        store2.sell("order2.dat");
//------------------------------------------------------------
        System.out.println();
    }

    private static void saveDelivery(File file, Delivery del){
        String s = JSON.toJSONString(del);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            assert file.createNewFile();
            byte[] b = s.getBytes();
            fos.write(b);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Delivery newDelivery(){
        Delivery delivery = new Delivery();
        Delivery.Fruits fruits;
        Random random = new Random();
        for (int i = 0; i < 200; i++) {
            fruits = new Delivery.Fruits();
            fruits.date = new Date();
            fruits.type = KindOfFruit.values()[random.nextInt(KindOfFruit.values().length)];
            fruits.shelfLife = random.nextInt(20);
            fruits.price = new BigDecimal(random.nextInt(50));
            delivery.addDelivery(fruits);
        }
        return delivery;
    }

    private static void addClients(String orderFile){
        Customers clientGroup = new Customers();
        Random random = new Random();
        int countOfCustomers = random.nextInt(5);
        Customers.Order order;

        for (int i = 0; i < countOfCustomers; i++) {
            order = new Customers.Order();
            order.name = "Customer #"+(i+1);
            order.type = KindOfFruit.values()[random.nextInt(KindOfFruit.values().length)];
            order.count = random.nextInt(50);
            clientGroup.addClient(order);
        }

        String j = JSON.toJSONString(clientGroup);
        File file = new File(orderFile);
        try {
            ServiceShop.saveJSONtoFile(j, file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
