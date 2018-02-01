package homework7.shop;

import com.alibaba.fastjson.JSON;
import homework7.cucl.Fruits;
import homework7.cucl.KindOfFruit;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Companyes {
    public List<Store> stores = new ArrayList<>();
    public BigDecimal moneyBalances = new BigDecimal(0);
    private File currentFile;

    /**
     * addStore(File storeFile)
     * addStore(Store store)
     * add store from store.dat file, or Store class
     */
    public void addStore(File storeFile){
        try {
            String jsonStore = ServiceShop.loadFromFileToJSONString(storeFile);
            Store store = JSON.parseObject(jsonStore, Store.class);
            stores.add(store);
            moneyBalances = moneyBalances.add(store.moneyBalance);
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
        Companyes temp = this;
        String thisJSONString = JSON.toJSONString(temp);
        ServiceShop.saveJSONtoFile(thisJSONString, new File(companyFile));
        currentFile = new File(companyFile);
    }

    /**
     * LOAD all info to this @stores and @moneyBalances
     */
    public void load(String companyFile){
        File file = new File(companyFile);
        try {
            String JSONstring = ServiceShop.loadFromFileToJSONString(file);
            Companyes companyes = JSON.parseObject(JSONstring, Companyes.class);
            stores.clear();
            stores.addAll(companyes.stores);
            moneyBalances = new BigDecimal(0);
            moneyBalances = moneyBalances.add(companyes.moneyBalances);
            currentFile = new File(companyFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return store as index
     */
    public Store getStore(int index) {
        return stores.get(index);
    }

    /**
     * @return (and set to this.moneyBalances) sum of all moneyBalances stores
     */
    public BigDecimal getCompanyBalance(){
        setMoneyBalance();
        return moneyBalances;
    }
    private void setMoneyBalance(){
        moneyBalances = new BigDecimal(0);
        for (Store s:
             stores) {
            moneyBalances = moneyBalances.add(s.moneyBalance);
        }
    }

    List<Fruits> getSpoiledFruits(Date date){
        List<Fruits> getList = new ArrayList<>();
        for (Store s:
             stores) {
            getList.addAll(s.getSpoiledFruits(date));
        }
        return getList;
    }

    List<Fruits> getSpoiledFruits(Date date, KindOfFruit type){
        List<Fruits> getList = new ArrayList<>();
        for (Store s:
             stores) {
            getList.addAll(s.getSpoiledFruits(date, type));
        }
        return getList;
    }

    List<Fruits> getAvailableFruits(Date date){
        List<Fruits> getList = new ArrayList<>();
        for (Store s:
             stores) {
            getList.addAll(s.getAvailableFruits(date));
        }
        return getList;
    }

    List<Fruits> getAvailableFruits(Date date, KindOfFruit type){
        List<Fruits> getList = new ArrayList<>();
        for (Store s:
             stores) {
            getList.addAll(s.getAvailableFruits(date, type));
        }
        return getList;
    }

    List<Fruits> getAddedFruits(Date date){
        List<Fruits> getList = new ArrayList<>();
        for (Store s:
             stores) {
            getList.addAll(s.getAddedFruits(date));
        }
        return getList;
    }

    List<Fruits> getAddedFruits(Date date, KindOfFruit type){
        List<Fruits> getList = new ArrayList<>();
        for (Store s:
             stores) {
            getList.addAll(s.getAddedFruits(date, type));
        }
        return getList;
    }



}
