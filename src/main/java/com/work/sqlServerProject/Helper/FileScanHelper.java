package com.work.sqlServerProject.Helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Created by Александр on 11.07.2019.
 */
public class FileScanHelper {

    public static List<String> getFiles(String pathToDirectory){
        Path path = Paths.get(pathToDirectory);
        List<String>resFiles=new ArrayList<>();
        try {
            List<String>files= Files.walk(path)./*filter(p->p.toString().endsWith(".nmf")).*/map(p->p.toString())./*sorted(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    String dateS1=o1.substring(o1.lastIndexOf("_")+1,o1.lastIndexOf(" "));
                    String dateS2=o2.substring(o2.lastIndexOf("_")+1,o2.lastIndexOf(" "));
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMMdd", Locale.ENGLISH);
                    LocalDate dateTime = LocalDate.parse(dateS1, formatter);
                    LocalDate dateTime2 = LocalDate.parse(dateS2, formatter);
                    return dateTime.compareTo(dateTime2);
                }
            }).*/collect(Collectors.toList());
            resFiles=files.subList(0,5);

        } catch (IOException e) {
            System.out.println("неправильно указана директория с файлами .nmf");
        }
        return resFiles;
    }
}
