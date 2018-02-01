package homework7.shop;

import com.alibaba.fastjson.JSON;
import homework7.cucl.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class Store {
    /**
     * @Delivery - поточна(остання) поставка фруктів.
     *             При виклику метода addFruits(String patchFile) фрукти з файлу поставки заносяться в @fruitList
     * @fruitslist - безпосередньо "склад" магазину. Містить всі фрукти.
     *              Може перезавантажуватись з файлу в методі load(String patchFile).
     *              Записується в файл методом save(String patchFile)
     */
    private Delivery delivery = new Delivery();
    private List<Fruits> fruitsList = new ArrayList<>();
    private BigDecimal moneyBalance = new BigDecimal(0);
    private StoreDB storeDB;

    /**
     * @param stringFile - шлях до файлу, в який завантажується JSON поточного класу shop,
     *                   тобто весь склад (fruitList)
     * @param currentFile - використовується в методі sell(String patchToJsonFile), так як потрібно перезаписувати
     *                    головний файл складу
     */
    private File currentFile;
    public void save(String storeFile) {
        File file = new File(storeFile);
//        if (file.exists()) {
//            copyBakFile(file);
//        }
//        try {
//            assert (file.createNewFile());
//            String jsonThis = JSON.toJSONString(this);
//            saveToFile(file, jsonThis);
//            currentFile = file;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            String jsonStoreDB = JSON.toJSONString(storeDB);
            ServiceShop.saveJSONtoFile(jsonStoreDB, file);
            currentFile = file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//
    }

    /**
     * @param storeFile - шлях до файлу, з якого завантажується (перезавантажується) весь склад (fruitList) + moneyBalances.
     * @throws FileNotFoundException
     */

    public void load(String storeFile) throws FileNotFoundException{
        File file = new File(storeFile);
        currentFile = new File(file.getName());
        String s = loadJSON(file);
        StoreDB temp = JSON.parseObject(s, StoreDB.class);

        storeDB.fruitsList = new ArrayList<>(temp.fruitsList);

        storeDB.moneyBalance = new BigDecimal(0);
        storeDB.moneyBalance = storeDB.moneyBalance.add(temp.moneyBalance);

        fillThis(storeDB);

        currentFile = new File(storeFile);
    }

    /**
     * продаж.
     * Використовується
     * @param pathToJsonFile - файл з замовленням
     * @throws FileNotFoundException
     */
    void sell(String pathToJsonFile) throws FileNotFoundException{
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        File file = new File(pathToJsonFile);
        String jsonGroup = loadJSON(file);
        Customers group;
        group = JSON.parseObject(jsonGroup, Customers.class);
        for (Customers.Order c:
             group.orderList) {
            List<Fruits> fruits = getAvailableFruits(new Date(), c.type);
            if (fruits.size() <= c.count) {
                System.out.printf("Для замолення %s з %s- %d %s не вистачає товару. Кількість на складі = %d\n" +
                                "замовлення відхилено. %s\n",
                        c.name, pathToJsonFile, c.count, c.type, (fruits.size()+1), currentFile.getName());
                continue;
            }
            int count = c.count;
            for (Fruits f:
                 fruits) {
                storeDB.moneyBalance = storeDB.moneyBalance.add(f.price);
                storeDB.fruitsList.remove(f);
                if (--count == 0) break;
            }
        }
        fillThis(storeDB);
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
        try {
            storeDB.fruitsList.addAll(delivery.fruitsList);
        } catch (Exception e) {
            storeDB = new StoreDB();
            storeDB.fruitsList = new ArrayList<>();
            storeDB.fruitsList.addAll(delivery.fruitsList);
        }
        fillThis(storeDB);
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
                getSpoiledFruits(date)) {
            if (f.type == type) temp.add(f);
        }
        return temp;
    }

    public List<Fruits> getAvailableFruits(Date date){
        List<Fruits> temp = new ArrayList<>();
        for (Fruits f:
             this.fruitsList) {
            Date a = new Date(f.date.getTime() + (24*60*60*1000*f.shelfLife));
            if (a.after(date)) temp.add(f);
        }
        return temp;
    }
    public List<Fruits> getAvailableFruits(Date date, KindOfFruit type){
        List<Fruits> temp = new ArrayList<>();
        for (Fruits f:
             getAvailableFruits(date)) {
            if (f.type == type) temp.add(f);
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

    public List<Fruits> getAddedFruits(Date date, KindOfFruit type){
        List<Fruits> temp = new ArrayList<>();
        for (Fruits f:
             getAddedFruits(date)) {
            if (f.type == type)
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
        scanner.close();
        return String.valueOf(sb);
    }

    List<Fruits> getFruitsList(){
        return fruitsList;
    }

    void setFruitsList(List<Fruits> fruitsList){
        this.fruitsList = new ArrayList<>(fruitsList);
    }

    void setMoneyBalance(BigDecimal moneyBalance){
        this.moneyBalance = new BigDecimal(moneyBalance.doubleValue());
    }


    private void fillThis(StoreDB storeDB){
        this.moneyBalance = new BigDecimal(0);
        this.moneyBalance = moneyBalance.add(storeDB.moneyBalance);

        this.fruitsList = new ArrayList<>(storeDB.fruitsList);
    }
}

