package homework7.shop;

import com.alibaba.fastjson.JSON;
import homework7.cucl.CompanyDB;
import homework7.cucl.Fruits;
import homework7.cucl.KindOfFruit;
import homework7.cucl.StoreDB;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Companyes {
//    private List<Store> storesList = new ArrayList<>();
    private List<StoreDB> storeDBList = new ArrayList<>();
    private BigDecimal companyBalances = new BigDecimal(0);
    private File currentFile;
    private CompanyDB companyDB = new CompanyDB();


    /**
     * !!!
     * copy moneyBalance, storeDBList from CompanyDB to this.companyBalance and this.storeDBList
     * @param companyDB
     */
    private void fillThis(CompanyDB companyDB){
        storeDBList = new ArrayList<>(companyDB.storeList);

        companyBalances = new BigDecimal(0);
        companyBalances = companyBalances.add(companyDB.moneyBalance);
    }

    /**
     * addStore(File storeFile)
     * addStore(Store store)
     * add store from store.dat file, or Store class
     */
    public void addStore(File storeFile){
        try {
            String jsonStore = ServiceShop.loadFromFileToJSONString(storeFile);
            StoreDB storeDB = JSON.parseObject(jsonStore, StoreDB.class);
            companyDB.storeList.add(storeDB);
            companyDB.moneyBalance = companyDB.moneyBalance.add(storeDB.moneyBalance);
//            String thisString = JSON.toJSONString(this);
//            if (currentFile == null) currentFile = new File("tempCompany.dat");
//            ServiceShop.saveJSONtoFile(thisString, currentFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        fillThis(companyDB);
    }

    /**
     * SAVE all info about Companyes
     */
    public void saveToFile(String companyFile) throws FileNotFoundException{
        String companyDBJSONString = JSON.toJSONString(companyDB);
        ServiceShop.saveJSONtoFile(companyDBJSONString, new File(companyFile));
        currentFile = new File(companyFile);
    }

    /**
     * LOAD all info to companyDB @stores and @moneyBalances
     */
    public void load(String companyFile){
        File file = new File(companyFile);
        try {
            String JSONstring = ServiceShop.loadFromFileToJSONString(file);
            CompanyDB cDB = JSON.parseObject(JSONstring, CompanyDB.class);
            companyDB.storeList = new ArrayList<>(cDB.storeList);

            companyDB.moneyBalance = new BigDecimal(0);
            companyDB.moneyBalance = companyDB.moneyBalance.add(cDB.moneyBalance);
            currentFile = new File(companyFile);
            fillThis(companyDB);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return store as index
     */
    public Store getStore(int index) {
        Store store = new Store();
        StoreDB storeDB = companyDB.storeList.get(index);
        store.setFruitsList(storeDB.fruitsList);
        store.setMoneyBalance(storeDB.moneyBalance);
        return store;
    }

    /**
     * @return (and set to companyDB.moneyBalances) sum of all moneyBalances stores
     */
    public BigDecimal getCompanyBalance(){
        setMoneyBalance();
        return companyDB.moneyBalance;
    }

    private void setMoneyBalance(){
        companyDB.moneyBalance = new BigDecimal(0);
        for (StoreDB s:
             companyDB.storeList) {
            companyDB.moneyBalance = companyDB.moneyBalance.add(s.moneyBalance);
        }
        fillThis(companyDB);
    }

    private final int ONE_DAY = 24*60*60*1000;

    public List<Fruits> getSpoiledFruits(Date date){
        List<Fruits> getList = new ArrayList<>();
        for (StoreDB s:
             companyDB.storeList) {
            for (Fruits f:
                 s.fruitsList) {
                if (date.after(new Date(f.date.getTime() + ONE_DAY*f.shelfLife)))
                    getList.add(f);
            }
        }
        return getList;
    }

    public List<Fruits> getSpoiledFruits(Date date, KindOfFruit type){
        List<Fruits> getList = new ArrayList<>();
        for (Fruits f:
                getSpoiledFruits(date)) {
            if (f.type == type) getList.add(f);
        }
        return getList;
    }

    List<Fruits> getAvailableFruits(Date date){
        List<Fruits> getList = new ArrayList<>();
        for (StoreDB s:
                companyDB.storeList) {
            for (Fruits f:
                    s.fruitsList) {
                if (date.before(new Date(f.date.getTime() + ONE_DAY*f.shelfLife)))
                    getList.add(f);
            }
        }
        return getList;
    }

    List<Fruits> getAvailableFruits(Date date, KindOfFruit type){
        List<Fruits> getList = new ArrayList<>();
        for (Fruits f:
                getAvailableFruits(date)) {
            if (f.type == type) getList.add(f);
        }
        return getList;
    }

    List<Fruits> getAddedFruits(Date date){
        List<Fruits> getList = new ArrayList<>();
        for (StoreDB s:
                companyDB.storeList) {
            for (Fruits f:
                    s.fruitsList) {
                if (date.getDate() == f.date.getDate())
                    getList.add(f);
            }
        }
        return getList;
    }

    List<Fruits> getAddedFruits(Date date, KindOfFruit type){
        List<Fruits> getList = new ArrayList<>();
        for (Fruits f:
                getAddedFruits(date)) {
            if (f.type == type) getList.add(f);
        }
        return getList;
    }
}
