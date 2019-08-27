package com.work.sqlServerProject.NBFparser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by a.shcherbakov on 30.05.2019.
 */
public class ParserHalper {
    public static List<String> createinSrtings(String pathToScan) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String lineSeparator = System.lineSeparator();
        List<String>resList=new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(pathToScan))));
            String s = reader.readLine();
            while (s!=null){
                //System.out.println(s);
                stringBuilder.append(s);
                stringBuilder.append(lineSeparator);
                s=reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файл сканера не найден");
        } catch (IOException e) {
            System.out.println("ошибка при чтении файла сканера");
        }
        String[] points = stringBuilder.toString().split("GPS");
        for (String s : points){
            resList.add("GPS"+s);
        }
        resList.remove(0);

        return resList;

    }
}
