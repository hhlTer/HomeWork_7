package homework7.shop;

import com.alibaba.fastjson.JSON;
import homework7.cucl.CompanyDB;
import homework7.cucl.Fruits;
import homework7.cucl.KindOfFruit;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Companyes {
    private List<Store> storesList = new ArrayList<>();
    private BigDecimal companyBalances = new BigDecimal(0);
    private File currentFile;
    private CompanyDB companyDB = new CompanyDB();

    /**
     * addStore(File storeFile)
     * addStore(Store store)
     * add store from store.dat file, or Store class
     */
    public void addStore(File storeFile){
        try {
            String jsonStore = ServiceShop.loadFromFileToJSONString(storeFile);
            Store store = JSON.parseObject(jsonStore, Store.class);
            companyDB.storeList.add(store);
            companyDB.moneyBalance = companyDB.moneyBalance.add(store.moneyBalance);
//            String thisString = JSON.toJSONString(this);
//            if (currentFile == null) currentFile = new File("tempCompany.dat");
//            ServiceShop.saveJSONtoFile(thisString, currentFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
            CompanyDB c = JSON.parseObject(JSONstring, CompanyDB.class);
            companyDB.storeList.clear();
            companyDB.storeList.addAll(c.storeList);
            companyDB.moneyBalance = new BigDecimal(0);
            companyDB.moneyBalance = companyDB.moneyBalance.add(c.moneyBalance);
            currentFile = new File(companyFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return store as index
     */
    public Store getStore(int index) {
        return companyDB.storeList.get(index);
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
        for (Store s:
             companyDB.storeList) {
            companyDB.moneyBalance = companyDB.moneyBalance.add(s.moneyBalance);
        }
    }

    List<Fruits> getSpoiledFruits(Date date){
        List<Fruits> getList = new ArrayList<>();
        for (Store s:
             companyDB.storeList) {
            getList.addAll(s.getSpoiledFruits(date));
        }
        return getList;
    }

    List<Fruits> getSpoiledFruits(Date date, KindOfFruit type){
        List<Fruits> getList = new ArrayList<>();
        for (Store s:
                companyDB.storeList) {
            getList.addAll(s.getSpoiledFruits(date, type));
        }
        return getList;
    }

    List<Fruits> getAvailableFruits(Date date){
        List<Fruits> getList = new ArrayList<>();
        for (Store s:
                companyDB.storeList) {
            getList.addAll(s.getAvailableFruits(date));
        }
        return getList;
    }

    List<Fruits> getAvailableFruits(Date date, KindOfFruit type){
        List<Fruits> getList = new ArrayList<>();
        for (Store s:
                companyDB.storeList) {
            getList.addAll(s.getAvailableFruits(date, type));
        }
        return getList;
    }

    List<Fruits> getAddedFruits(Date date){
        List<Fruits> getList = new ArrayList<>();
        for (Store s:
                companyDB.storeList) {
            getList.addAll(s.getAddedFruits(date));
        }
        return getList;
    }

    List<Fruits> getAddedFruits(Date date, KindOfFruit type){
        List<Fruits> getList = new ArrayList<>();
        for (Store s:
                companyDB.storeList) {
            getList.addAll(s.getAddedFruits(date, type));
        }
        return getList;
    }



}
