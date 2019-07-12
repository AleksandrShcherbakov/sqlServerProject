package com.work.sqlServerProject.Helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
        Comparator<Path> comparator=new Comparator <Path>() {
            @Override
            public int compare(Path o1, Path o2) {
                BasicFileAttributes atr1 = null;
                try {
                    atr1 = Files.readAttributes(o1, BasicFileAttributes.class);
                    System.out.println(atr1.creationTime());
                } catch (IOException e) {
                    System.out.println(o1+" нет атрибутов");
                }
                BasicFileAttributes atr2 = null;
                try {
                    atr2 = Files.readAttributes(o1, BasicFileAttributes.class);
                } catch (IOException e) {
                    System.out.println(o2+" нет атрибутов");
                }
                return atr2.creationTime().compareTo(atr1.creationTime());
            }
        };

        Comparator comparator1=new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String dateS1="";
                String dateS2="";
                try {
                    dateS1=o1.substring(o1.lastIndexOf("_")+1,o1.lastIndexOf(" "));
                }
                catch (StringIndexOutOfBoundsException e){
                    System.out.println(o1);
                }
                try {
                    dateS2=o2.substring(o2.lastIndexOf("_")+1,o2.lastIndexOf(" "));
                }
                catch (StringIndexOutOfBoundsException e){
                    System.out.println(o2);
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMMdd", Locale.ENGLISH);
                LocalDate dateTime;
                LocalDate dateTime2;
                try {
                    dateTime = LocalDate.parse(dateS1, formatter);
                    dateTime2 = LocalDate.parse(dateS2, formatter);
                }
                catch (DateTimeParseException e){
                    return 1;
                }
                return dateTime2.compareTo(dateTime);
            }
        };
        try {
            List<Path>files= Files.walk(path).filter(p->Files.isRegularFile(p) && p.toString().endsWith("1.nmf")).peek(p-> System.out.println(p.toString())).collect(Collectors.toList());
            files.sort(comparator);
            resFiles=files.stream().map(p->p.toString()).collect(Collectors.toList());
            resFiles=resFiles.subList(resFiles.size()-3,resFiles.size());

        } catch (IOException e) {
            System.out.println("неправильно указана директория с файлами .nmf");
        }
        return resFiles;
    }

}
