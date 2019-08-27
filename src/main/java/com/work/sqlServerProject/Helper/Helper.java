package com.work.sqlServerProject.Helper;

import com.work.sqlServerProject.model.CellForSZ;
import com.work.sqlServerProject.model.CellInfo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by a.shcherbakov on 19.02.2019.
 */
public class Helper {
    public static void writeToCSV(String filePath, String data){
        if (new File(filePath).isDirectory()){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy");

            String fileName= simpleDateFormat.format(new Date())+"BS.csv";
            try{
                FileOutputStream outputStream = new FileOutputStream(filePath+"\\"+fileName);
                outputStream.write(data.getBytes());
                outputStream.write("\n".getBytes());
                outputStream.write(data.getBytes());
                outputStream.close();
            }
            catch (IOException s) {

            }
        }
    }

    public static String writeToCSV(String filePath, List<CellInfo>list){
        if (new File(filePath).isDirectory()){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy");

            String fileName= simpleDateFormat.format(new Date())+"_ALL_BS.csv";

            try{
                FileOutputStream outputStream = new FileOutputStream(filePath+"\\"+fileName,true);
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream,"Cp1251"));
                String lineSeparator = System.getProperty("line.separator");
                printWriter.print("SYSTEM;SITE;CID;LAT;LON;CELL;LAC;TAC;BAND;BSC;CH;BSIC;SCR;PCI;DIR;HEIGHT;TILT;RNCID;REGION");
                printWriter.print(lineSeparator);
                for (CellInfo a : list) {
                    printWriter.print(a);
                    printWriter.print(lineSeparator);
                }
                printWriter.flush();
                printWriter.close();
            } catch (FileNotFoundException e) {
                return "������ �� "+list.getClass().getSimpleName()+" �� ��������, �������� ���������� FileNotFoundException";
            } catch (IOException e) {
                return "������ �� "+list.getClass().getSimpleName()+" �� ��������, �������� ���������� IOException";
            }

        }
        return "������ �� "+list.getClass().getSimpleName()+" �������� � ����";
    }

    public static List<CellForSZ> setCIfromform(List<CellForSZ>list, List<CellForSZ> form){
        List<CellForSZ> result = new ArrayList<>();
        for (int i=0;i<list.size();i++){
            CellForSZ cellFromList=list.get(i);
            CellForSZ cellFromForm=form.get(i);
            cellFromList.setCIinNetwork(cellFromForm.getCIinNetwork());
            result.add(i,cellFromList);
        }
        return result;
    }

    public static String createDefPath() throws IOException {
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfMonth = new SimpleDateFormat("MMMMMMMMMMMM");
        Date date = new Date();
        String defPath= "C://��������� �������/"+sdfYear.format(date)+"/"+sdfMonth.format(date)+"/"+sdfDate.format(date);
        File fileDir = new File(defPath);
        if (!fileDir.exists()){
            Files.createDirectories(Paths.get(defPath));
        }
        return defPath;
    }

    public static String createDefPath(String path) throws IOException {
        File fileDir = new File(path);
        if (!fileDir.exists()){
            Files.createDirectories(Paths.get(path));
        }
        return path;
    }
}
