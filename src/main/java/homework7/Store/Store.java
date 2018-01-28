package homework7.Store;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

class Store {
    Store(){
        fruitsList = new ArrayList<>();
    }
    private List<Fruits> fruitsList;

    public void save(String stringFile) {
        File file = new File(stringFile);
        if (file.exists()) {
            copyBakFile(file);
            assert (file.delete());
        }
        try {
            assert (file.createNewFile());
            Store store = this;
            String jsonThis = JSON.toJSONString(store);
            saveToFile(file, jsonThis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void addFruits(Fruits fruits){
        fruitsList.add(fruits);
    }

    private void saveToFile(File file, String string){
        try {
            FileOutputStream fos = new FileOutputStream(file);
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
            saveToFile(bakFile, String.valueOf(sb));
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
