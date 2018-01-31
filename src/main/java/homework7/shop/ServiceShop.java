package homework7.shop;

import java.io.*;
import java.util.*;

class ServiceShop {
    /**
     * @param JSONString
     * @param file
     * @throws FileNotFoundException
     */
    static void saveJSONtoFile(String JSONString, File file)throws FileNotFoundException{
        FileOutputStream fos = new FileOutputStream(file);
        if (file.exists() & !file.getName().endsWith(".bak")) copyBakFile(file);

        byte[] b = JSONString.getBytes();
        try {
            assert file.createNewFile();
            fos.write(b);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * copy file to .bak file
     * @param file
     */
    private static void copyBakFile(File file){
        File bakFile = new File(file.getName() + ".bak");
        try {
            assert (bakFile.createNewFile());
            String s = loadFromFileToJSONString(file);
            saveJSONtoFile(s, bakFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String loadFromFileToJSONString(File file) throws FileNotFoundException{
        if (!file.canRead()) {
            System.err.println("Read file Error");
            throw new FileNotFoundException();
        }
        Scanner scanner = new Scanner(new FileInputStream(file));
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNext()){
            sb.append(scanner.nextLine());
        }
        return String.valueOf(sb);
    }
}
