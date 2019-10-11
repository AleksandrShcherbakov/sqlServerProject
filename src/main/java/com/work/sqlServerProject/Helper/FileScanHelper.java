package com.work.sqlServerProject.Helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by јлександр on 11.07.2019.
 */
public class FileScanHelper {
    public static Comparator<Path> comparator=new Comparator <Path>() {
        @Override
        public int compare(Path o1, Path o2) {
            BasicFileAttributes atr1 = null;
            try {
                atr1 = Files.readAttributes(o1, BasicFileAttributes.class);
                //System.out.println(atr1.creationTime());
            } catch (IOException e) {
                System.out.println(o1+" нет атрибутов");
            }
            BasicFileAttributes atr2 = null;
            try {
                atr2 = Files.readAttributes(o2, BasicFileAttributes.class);
            } catch (IOException e) {
                System.out.println(o2+" нет атрибутов");
            }
            //System.out.println(atr2.creationTime().compareTo(atr1.creationTime()));
            return atr2.creationTime().compareTo(atr1.creationTime());
        }
    };

    public static List<Path> getFiles(String pathToDirectory){
        Path path = Paths.get(pathToDirectory);
        List<Path>resFiles=new ArrayList<>();


        List<Path>files= null;
        try {
            files = Files.walk(path).filter(p->Files.isRegularFile(p) && p.toString().endsWith("1.nmf")).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (files!=null) {
            files.sort(comparator);
            if (files.size() >= 3) {
                resFiles = files.subList(0, 3);
            }
            else resFiles=files;
        }
        return resFiles;
    }
}
