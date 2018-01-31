package homework7.shop;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.List;

public class Company {
    private List<Store> stores;
    private BigDecimal moneyBalance;

    /**
     * SAVE all info about Company
     */
    public void save(String patchFile) throws FileNotFoundException{
        String thisJSONString = JSON.toJSONString(this);
        ServiceShop.saveJSONtoFile(thisJSONString, new File(patchFile));
    }
    /**
     * LOAD all info to this @stores and @moneyBalance
     */
    public void load(String patchFile){
        File file = new File(patchFile);
        try {
            String JSONstring = ServiceShop.loadFromFileToJSONString(file);
            Company company = JSON.parseObject(JSONstring, Company.class);
            stores.clear();
            stores.addAll(company.stores);
            moneyBalance = new BigDecimal(0);
            moneyBalance = moneyBalance.add(company.moneyBalance);
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
     * @return (and set to this.moneyBalance) sum of all moneyBalance stores
     */
    public BigDecimal getCompanyBalance(){
        setMoneyBalance();
        return moneyBalance;
    }
    private void setMoneyBalance(){
        for (Store s:
             stores) {
            moneyBalance = new BigDecimal(0);
            moneyBalance = moneyBalance.add(s.moneyBalance);
        }
    }


}
