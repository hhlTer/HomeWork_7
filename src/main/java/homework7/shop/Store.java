package homework7.shop;

import com.alibaba.fastjson.JSON;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

class Store {
    /**
     * @Delivery - поточна(остання) поставка фруктів.
     *             При виклику метода addFruits(String patchFile) фрукти з файлу поставки заносяться в @fruitList
     * @fruitslist - безпосередньо "склад" магазину. Містить всі фрукти.
     *              Може перезавантажуватись з файлу в методі load(String patchFile).
     *              Записується в файл методом save(String patchFile)
     */
    private Delivery delivery = new Delivery();
    public List<Fruits> fruitsList = new ArrayList<>();
    public BigDecimal moneyBalance = new BigDecimal(0);


    /**
     * @param stringFile - шлях до файлу, в який завантажується JSON поточного класу shop,
     *                   тобто весь склад (fruitList)
     * @param currentFile - використовується в методі sell(String patchToJsonFile), так як потрібно перезаписувати
     *                    головний файл складу
     */
    private File currentFile;
    public void save(String stringFile) {
        File file = new File(stringFile);
        if (file.exists()) {
            copyBakFile(file);
        }
        try {
            assert (file.createNewFile());
            String jsonThis = JSON.toJSONString(this);
            saveToFile(file, jsonThis, true);
            currentFile = file;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param patchToJsonFile - шлях до файлу, з якого завантажується (перезавантажується) весь склад (fruitList).
     * @throws FileNotFoundException
     */

    public void load(String patchToJsonFile) throws FileNotFoundException{
        File file = new File(patchToJsonFile);
        currentFile = file;
        String s = loadJSON(file);
        Store temp = JSON.parseObject(s, Store.class);
        this.fruitsList.clear();
        this.fruitsList.addAll(temp.fruitsList);
        moneyBalance = temp.moneyBalance;
    }

    /**
     * продаж.
     * Використовується
     * @param pathToJsonFile - файл з замовленням
     * @throws FileNotFoundException
     */
    void sell(String pathToJsonFile) throws FileNotFoundException{
        File file = new File(pathToJsonFile);
        String jsonGroup = loadJSON(file);
        ClientGroup group;
        group = JSON.parseObject(jsonGroup, ClientGroup.class);
        for (Order c:
             group.orderList) {
            List<Fruits> fruits = getAvailableFruits(new Date(), c.type);
            if (fruits.size() <= c.count) {
                System.out.printf("Для замолення %s - %d %s не вистачає товару. Кількість на складі = %d\n" +
                                "замовлення відхилено\n",
                        c.name, c.count, c.type, fruits.size());
                continue;
            }
            int count = c.count;
            for (Fruits f:
                 fruits) {
                moneyBalance = moneyBalance.add(f.price);
                fruitsList.remove(f);
                if (--count == 0) break;
            }

        }
        save(this.currentFile.getName());
    }
    /**
     * add fruits ftom file to delivery. One file - one delivery
     * @param patchToJsonFile
     * @throws FileNotFoundException
     */
    public void addFruits(String patchToJsonFile) throws FileNotFoundException{
        File file = new File(patchToJsonFile);
        delivery = JSON.parseObject(loadJSON(file), Delivery.class);
        fruitsList.addAll(delivery.fruitsList);
    }

    /**
     * getSpoiledFruit -
     * @return fruit type, witch shelf life is out of @param date
     * getAvailableFruits -
     * @return all fruit, with available using before date
     */
    public List<Fruits> getSpoiledFruits(Date date){
        List<Fruits> temp = new ArrayList<>();
        for (Fruits f:
                fruitsList) {
            Date a = new Date(f.date.getTime() + (24*60*60*1000*f.shelfLife));
            if (a.before(date)) temp.add(f);
        }
        return temp;
    }
    public List<Fruits> getSpoiledFruits(Date date, KindOfFruit type){
        List<Fruits> temp = new ArrayList<>();
        for (Fruits f:
                fruitsList) {
            Date a = new Date(f.date.getTime() + (24*60*60*1000*f.shelfLife));
            if (a.before(date) & f.type == type) temp.add(f);
        }
        return temp;
    }

    public List<Fruits> getAvailableFruits(Date date){
        List<Fruits> temp = new ArrayList<>();
        for (Fruits f:
             fruitsList) {
            Date a = new Date(f.date.getTime() + (24*60*60*1000*f.shelfLife));
            if (a.after(date)) temp.add(f);
        }
        return temp;
    }
    public List<Fruits> getAvailableFruits(Date date, KindOfFruit type){
        List<Fruits> temp = new ArrayList<>();
        for (Fruits f:
             fruitsList) {
            Date a = new Date(f.date.getTime() + (24*60*60*1000*f.shelfLife));
            if (a.after(date) & f.type == type) temp.add(f);
        }
        return temp;
    }

    public List<Fruits> getAddedFruits(Date date){
        List<Fruits> temp = new ArrayList<>();
        for (Fruits f:
             fruitsList) {
            if (f.date.getDate() == date.getDate())
                temp.add(f);
        }
        return temp;
    }
    public List<Fruits> getAddedFruits(Date date, Fruits fruits){
        List<Fruits> temp = new ArrayList<>();
        for (Fruits f:
             fruitsList) {
            if (f.date.getDate() == date.getDate() & f.type == fruits.type)
                temp.add(f);
        }
        return temp;
    }

    /**
     * add fruit to store - this.fruitList
     * @param fruits
     */

    public void addToStore(Fruits fruits){
        fruitsList.add(fruits);
    }

    /**
     * add fruits to Delivery
     * @param fruits
     */
    void addFruitsToDelivery(Fruits fruits){
        delivery.addDelivery(fruits);
    }


    /**
     * using inside shop class, by methods :
     *      load(String patchToJsonFile)
     *      addFruits(String patchToJsonFile)
     * @param file - file, which load into JSON string
     * @return JSON string
     * @throws FileNotFoundException
     */

    private String loadJSON(File file) throws FileNotFoundException{
        if (!file.exists()) throw new FileNotFoundException();
        if (!file.canRead()) {
            System.err.println("Read file error");
            System.exit(1);
        }
        Scanner scanner = new Scanner(new FileInputStream(file));
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNext()){
            sb.append(scanner.nextLine());
        }
        return String.valueOf(sb);
    }


    private void saveToFile(File file, String string, boolean append){
        try {
            FileOutputStream fos = append? new FileOutputStream(file, append) : new FileOutputStream(file);
            byte[] byteString = string.getBytes();
            fos.write(byteString);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void copyBakFile(File file){
        File bakFile = new File(file.getName() + ".bak");
        try {
            assert (bakFile.createNewFile());
            Scanner scanner = new Scanner(new FileInputStream(file));
            StringBuilder sb = new StringBuilder();
            while (scanner.hasNext()) {
                sb.append(scanner.nextLine());
            }
            saveToFile(bakFile, String.valueOf(sb), false);
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

class Delivery {
    public List<Fruits> fruitsList = new ArrayList<>();
    public void addDelivery(Fruits fruits){
        fruitsList.add(fruits);
    }
}
class ClientGroup{
    public List<Order> orderList = new ArrayList<>();
    public void addClient(Order client){ orderList.add(client);}
}